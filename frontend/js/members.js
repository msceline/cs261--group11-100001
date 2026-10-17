(() => {
  const start = () => {
    if (!document.body || document.getElementById('project-members-page')) return;

    const page = document.createElement('main');
    page.id = 'project-members-page';
    page.innerHTML = `
      <h1>สมาชิกในโปรเจกต์</h1>
      <form id="project-form">
        <label for="project-id">Project ID</label>
        <input id="project-id" name="projectId" type="number" min="1" required>
        <button type="submit">แสดงสมาชิก</button>
      </form>
      <p id="members-message" role="status" aria-live="polite"></p>
      <table>
        <thead>
          <tr>
            <th scope="col">ชื่อ</th>
            <th scope="col">รหัสนักศึกษา</th>
            <th scope="col">User ID</th>
            <th scope="col">หน้าที่</th>
          </tr>
        </thead>
        <tbody id="members-list"></tbody>
      </table>
      <h2>เพิ่มสมาชิก</h2>
      <form id="add-member-form">
        <label for="member-user-id">User ID</label>
        <input id="member-user-id" name="userId" type="number" min="1" required>
        <label for="member-role">หน้าที่</label>
        <select id="member-role" name="roleInProject">
          <option value="member">สมาชิก</option>
          <option value="visitor">ผู้เยี่ยมชม</option>
        </select>
        <button type="submit">เพิ่มสมาชิก</button>
      </form>
    `;
    document.body.append(page);

    const projectIdInput = page.querySelector('#project-id');
    const list = page.querySelector('#members-list');
    const message = page.querySelector('#members-message');
    const projectIdFromUrl = new URLSearchParams(window.location.search).get('projectId');
    const savedProjectId =
      localStorage.getItem('projectId') || localStorage.getItem('currentProjectId');
    projectIdInput.value = projectIdFromUrl || savedProjectId || '';

    function getToken() {
      return (
        localStorage.getItem('token') ||
        localStorage.getItem('authToken') ||
        localStorage.getItem('jwt')
      );
    }

    function selectedProjectId() {
      const projectId = Number(projectIdInput.value);
      if (!Number.isInteger(projectId) || projectId < 1) {
        throw new Error('กรุณาระบุ Project ID ที่ถูกต้อง');
      }
      return projectId;
    }

    function showError(error) {
      message.textContent = error instanceof Error ? error.message : String(error);
    }

    async function loadMembers() {
      if (!window.memberApi || typeof window.memberApi.getProjectMembers !== 'function') {
        throw new Error('ไม่พบ memberApi กรุณาโหลด ../api/member-api.js ก่อน members.js');
      }

      const projectId = selectedProjectId();
      localStorage.setItem('projectId', String(projectId));
      message.textContent = 'กำลังโหลดสมาชิก...';
      list.replaceChildren();

      const members = await window.memberApi.getProjectMembers(projectId, getToken());
      if (!Array.isArray(members)) {
        throw new Error('ข้อมูลสมาชิกที่ได้รับจากเซิร์ฟเวอร์ไม่ถูกต้อง');
      }

      if (members.length === 0) {
        message.textContent = 'ยังไม่มีสมาชิกในโปรเจกต์นี้';
        return;
      }

      const rows = members.map((member) => {
        const row = document.createElement('tr');
        [
          member.displayName || '-',
          member.studentId || '-',
          member.userId ?? '-',
          member.roleInProject || '-',
        ].forEach((value) => {
          const cell = document.createElement('td');
          cell.textContent = String(value);
          row.append(cell);
        });
        return row;
      });
      list.replaceChildren(...rows);
      message.textContent = `พบสมาชิก ${members.length} คน`;
    }

    page.querySelector('#project-form').addEventListener('submit', async (event) => {
      event.preventDefault();
      try {
        await loadMembers();
      } catch (error) {
        showError(error);
      }
    });

    page.querySelector('#add-member-form').addEventListener('submit', async (event) => {
      event.preventDefault();
      try {
        if (!window.memberApi || typeof window.memberApi.addProjectMember !== 'function') {
          throw new Error('ไม่พบ memberApi กรุณาโหลด ../api/member-api.js ก่อน members.js');
        }

        const projectId = selectedProjectId();
        const userId = Number(page.querySelector('#member-user-id').value);
        if (!Number.isInteger(userId) || userId < 1) {
          throw new Error('กรุณาระบุ User ID ที่ถูกต้อง');
        }

        message.textContent = 'กำลังเพิ่มสมาชิก...';
        await window.memberApi.addProjectMember(
          projectId,
          userId,
          page.querySelector('#member-role').value,
          getToken(),
        );
        page.querySelector('#member-user-id').value = '';
        await loadMembers();
      } catch (error) {
        showError(error);
      }
    });

    if (projectIdInput.value) {
      loadMembers().catch(showError);
    }
  };

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', start, { once: true });
  } else {
    start();
  }
})();
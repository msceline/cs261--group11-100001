const mainContainer = document.querySelector('.main-container');
const sidebar = mainContainer?.querySelector('aside');

if (mainContainer && sidebar) {
	let collapseTimer = null;

	sidebar.addEventListener('mouseenter', () => {
		if (collapseTimer !== null) return;
		mainContainer.classList.add('is-expanded');
	});

	sidebar.addEventListener('mouseleave', () => {
		if (collapseTimer !== null) return;

		collapseTimer = window.setTimeout(() => {
			mainContainer.classList.remove('is-expanded');
			collapseTimer = null;
		}, 2000);
	});
}



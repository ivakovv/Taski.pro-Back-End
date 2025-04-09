// Инициализация AOS (Animate On Scroll) библиотеки, если она подключена
document.addEventListener('DOMContentLoaded', function () {
	// Инициализация AOS
	if (typeof AOS !== 'undefined') {
		AOS.init({
			duration: 800,
			easing: 'ease-in-out',
			once: true,
		})
	}

	// Добавление тени к навигационной панели при скролле
	const header = document.querySelector('header')
	if (header) {
		window.addEventListener('scroll', function () {
			if (window.scrollY > 50) {
				header.querySelector('.navbar').classList.add('shadow')
			} else {
				header.querySelector('.navbar').classList.remove('shadow')
			}
		})
	}

	// Плавный скролл для якорных ссылок
	document.querySelectorAll('a[href^="#"]').forEach(anchor => {
		anchor.addEventListener('click', function (e) {
			e.preventDefault()

			const targetId = this.getAttribute('href')
			if (targetId === '#') return

			const targetElement = document.querySelector(targetId)
			if (targetElement) {
				const headerOffset = 80
				const elementPosition = targetElement.getBoundingClientRect().top
				const offsetPosition =
					elementPosition + window.pageYOffset - headerOffset

				window.scrollTo({
					top: offsetPosition,
					behavior: 'smooth',
				})

				// Закрываем мобильное меню при клике на ссылку
				const navbarToggler = document.querySelector('.navbar-toggler')
				const navbarCollapse = document.querySelector('.navbar-collapse')
				if (
					navbarToggler &&
					!navbarToggler.classList.contains('collapsed') &&
					navbarCollapse
				) {
					navbarToggler.classList.add('collapsed')
					navbarCollapse.classList.remove('show')
				}
			}
		})
	})

	// Активация пунктов меню при скролле
	const sections = document.querySelectorAll('section[id]')
	const navLinks = document.querySelectorAll('.navbar-nav .nav-link')

	function highlightNavLink() {
		const scrollPosition = window.scrollY

		sections.forEach(section => {
			const sectionTop = section.offsetTop - 100
			const sectionHeight = section.offsetHeight
			const sectionId = section.getAttribute('id')

			if (
				scrollPosition >= sectionTop &&
				scrollPosition < sectionTop + sectionHeight
			) {
				navLinks.forEach(link => {
					link.classList.remove('active')
					if (link.getAttribute('href') === '#' + sectionId) {
						link.classList.add('active')
					}
				})
			}
		})
	}

	window.addEventListener('scroll', highlightNavLink)
	highlightNavLink() // Вызов при загрузке страницы

	// Анимация подсчёта чисел
	function animateCounters() {
		const counters = document.querySelectorAll('.counter')

		counters.forEach(counter => {
			const target = parseInt(counter.getAttribute('data-count'))
			const duration = 2000 // 2 секунды
			const step = Math.ceil(target / (duration / 16)) // 60fps
			let current = 0

			const updateCounter = () => {
				current += step
				if (current < target) {
					counter.textContent = current
					requestAnimationFrame(updateCounter)
				} else {
					counter.textContent = target
				}
			}

			updateCounter()
		})
	}

	// Активация подсчёта при видимости элементов
	const observer = new IntersectionObserver(
		entries => {
			entries.forEach(entry => {
				if (entry.isIntersecting) {
					if (entry.target.classList.contains('counter-section')) {
						animateCounters()
					}
					observer.unobserve(entry.target)
				}
			})
		},
		{ threshold: 0.5 }
	)

	const counterSection = document.querySelector('.counter-section')
	if (counterSection) {
		observer.observe(counterSection)
	}

	// Эффекты наведения для карточек
	const cards = document.querySelectorAll('.hover-lift')
	cards.forEach(card => {
		card.addEventListener('mouseenter', function () {
			this.style.transform = 'translateY(-10px)'
			this.style.boxShadow = '0 15px 30px rgba(0, 0, 0, 0.1)'
		})

		card.addEventListener('mouseleave', function () {
			this.style.transform = ''
			this.style.boxShadow = ''
		})
	})

	// Изменение высоты хедера при скролле
	let lastScrollTop = 0

	window.addEventListener(
		'scroll',
		function () {
			const st = window.pageYOffset || document.documentElement.scrollTop

			if (st > lastScrollTop && st > 100) {
				// Скролл вниз
				document.querySelector('header .navbar').style.padding = '0.5rem 1rem'
			} else {
				// Скролл вверх
				document.querySelector('header .navbar').style.padding = '1rem'
			}

			lastScrollTop = st <= 0 ? 0 : st
		},
		false
	)

	// Подсветка поля формы при фокусе
	const formInputs = document.querySelectorAll('.form-control')
	formInputs.forEach(input => {
		input.addEventListener('focus', function () {
			this.parentElement.classList.add('input-focused')
		})

		input.addEventListener('blur', function () {
			this.parentElement.classList.remove('input-focused')
		})
	})

	// Эффект параллакса для фоновых элементов
	window.addEventListener('scroll', function () {
		const scrollPosition = window.pageYOffset
		const parallaxElements = document.querySelectorAll('.parallax')

		parallaxElements.forEach(element => {
			const speed = element.getAttribute('data-speed') || 0.5
			element.style.transform = `translateY(${scrollPosition * speed}px)`
		})
	})
})

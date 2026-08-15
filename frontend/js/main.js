/**
 * main.js — Global UI Behaviors
 *
 * Handles:
 * - Navbar scroll effect
 * - Mobile nav toggle
 * - Toast notifications
 * - Contact form submission
 */

// ----------------------------------------------------------------
// Navbar — add 'scrolled' class when user scrolls down
// ----------------------------------------------------------------

const navbar = document.getElementById('navbar');

window.addEventListener('scroll', () => {
    if (!navbar) return;
    if (window.scrollY > 50) {
        navbar.classList.add('scrolled');
    } else {
        navbar.classList.remove('scrolled');
    }
});

// ----------------------------------------------------------------
// Mobile Navigation Toggle
// ----------------------------------------------------------------

function toggleMobileNav() {
    const mobileNav = document.getElementById('mobileNav');
    if (!mobileNav) return;
    mobileNav.classList.toggle('open');
    document.body.style.overflow = mobileNav.classList.contains('open') ? 'hidden' : '';
}

// ----------------------------------------------------------------
// Toast Notifications
// ----------------------------------------------------------------

/**
 * Show a toast message at the bottom-right of the screen.
 *
 * @param {string} message - Text to show
 * @param {'success'|'error'|'info'} type - Visual style
 * @param {number} duration - How long to show in ms (default 3s)
 */
function showToast(message, type = 'info', duration = 3000) {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <span>${type === 'success' ? '✅' : type === 'error' ? '❌' : 'ℹ️'}</span>
        <span>${message}</span>
    `;

    container.appendChild(toast);

    // Remove after duration
    setTimeout(() => {
        toast.style.animation = 'none';
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(120%)';
        toast.style.transition = 'all .3s ease';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

// ----------------------------------------------------------------
// Contact Form Submission
// ----------------------------------------------------------------

/**
 * Submits the contact form data to the Spring Boot API.
 * Called by the form's onsubmit handler.
 */
async function submitContact(event) {
    event.preventDefault();

    const form = event.target;
    const submitBtn = form.querySelector('button[type="submit"]');

    const payload = {
        name:    form.name.value.trim(),
        contact: form.contact.value.trim(),
        message: form.message.value.trim()
    };

    // Disable button while sending
    submitBtn.textContent = 'Sending...';
    submitBtn.disabled = true;

    try {
        const response = await fetch('http://localhost:8080/api/contact', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            showToast('✅ Message sent! We will contact you soon.', 'success', 5000);
            form.reset();
        } else {
            const error = await response.json().catch(() => ({}));
            showToast(`❌ Failed to send: ${error.message || 'Please try again.'}`, 'error');
        }
    } catch (err) {
        // Backend might be offline — show a graceful message
        console.error('Contact form error:', err);
        showToast('⚠️ Could not connect to server. Please call us directly.', 'error', 6000);
    } finally {
        submitBtn.textContent = 'Send Message ✉️';
        submitBtn.disabled = false;
    }
}

// ----------------------------------------------------------------
// Active Nav Link — highlight the correct link based on current page
// ----------------------------------------------------------------

(function highlightActiveNav() {
    const current = window.location.pathname.split('/').pop() || 'index.html';
    document.querySelectorAll('.nav-links a').forEach(link => {
        const href = link.getAttribute('href');
        if (href && href.includes(current)) {
            link.classList.add('active');
        }
    });
})();

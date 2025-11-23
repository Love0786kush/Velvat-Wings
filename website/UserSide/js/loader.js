// Loader - Fixed version
window.addEventListener('load', function() {
    setTimeout(function() {
        const loader = document.getElementById("loader");
        if (loader) {
            loader.style.display = "none";
        }
    }, 1000);
});

// Search Toggle
const searchWrapper = document.getElementById("searchWrapper");
const searchIcon = document.getElementById("searchIcon");

if (searchWrapper && searchIcon) {
    searchIcon.addEventListener("click", (e) => {
        e.stopPropagation();
        searchWrapper.classList.toggle("active");
    });

    document.addEventListener("click", (e) => {
        if (!searchWrapper.contains(e.target)) {
            searchWrapper.classList.remove("active");
        }
    });
}

// Profile Menu Toggle
const userIconContainer = document.getElementById("userIconContainer");
const profileMenu = document.getElementById("profileMenu");

if (userIconContainer && profileMenu) {
    userIconContainer.addEventListener("click", (e) => {
        e.stopPropagation();
        profileMenu.classList.toggle("active");
    });

    document.addEventListener("click", () => {
        profileMenu.classList.remove("active");
    });
}

// Hero Slider
let slides = document.querySelectorAll(".hero-slide");
let index = 0;

if (slides.length > 0) {
    setInterval(() => {
        slides.forEach(slide => slide.classList.remove("active"));
        index = (index + 1) % slides.length;
        slides[index].classList.add("active");
    }, 4000);
}

// Cart Functionality
const cartIcon = document.getElementById("cartIcon");
const cartPopup = document.getElementById("cartPopup");
const closeCart = document.querySelector(".close-cart");
const cartItems = document.getElementById("cartItems");
const cartTotal = document.getElementById("cartTotal");
const cartCount = document.querySelector(".cart-count");
const checkoutBtn = document.querySelector(".checkout-btn");

let cart = [];
let total = 0;

if (cartIcon && cartPopup) {
    cartIcon.addEventListener("click", () => {
        cartPopup.classList.add("active");
    });
}

if (closeCart) {
    closeCart.addEventListener("click", () => {
        cartPopup.classList.remove("active");
    });
}

// Products Data
const products = [
    {
        id: "1",
        name: "Laptop",
        description: "Powerful gaming laptop",
        price: 55000,
        imageUrl: "./Assets/product1.jpg"
    },
    {
        id: "2",
        name: "Headphones",
        description: "Noise cancellation",
        price: 3500,
        imageUrl: "./Assets/product2.jpg"
    },
    {
        id: "3",
        name: "Shoes",
        description: "Running shoes",
        price: 2800,
        imageUrl: "./Assets/product3.jpg"
    },
    {
        id: "4",
        name: "Smart Watch",
        description: "Fitness tracker",
        price: 4200,
        imageUrl: "./Assets/banner-5.png"
    }
];

// Render Products - Only on index page
const productGrid = document.getElementById("productGrid");
if (productGrid) {
    products.forEach(product => {
        productGrid.innerHTML += `
            <div class="product-card">
                <img src="${product.imageUrl}" alt="${product.name}" onerror="this.src='https://via.placeholder.com/300x200?text=Product+Image'">
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <p class="price">₹${product.price}</p>
                <button onclick="addToCart('${product.id}')">Add to Cart</button>
            </div>
        `;
    });
}

// Add to Cart Function
function addToCart(productId) {
    const product = products.find(p => p.id === productId);
    if (!product) return;
    
    const existingItem = cart.find(item => item.id === productId);
    
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({...product, quantity: 1});
    }
    
    updateCart();
    alert(`Added: ${product.name}`);
}

// Update Cart
function updateCart() {
    if (cartItems && cartTotal && cartCount) {
        cartItems.innerHTML = '';
        total = 0;
        
        cart.forEach(item => {
            total += item.price * item.quantity;
            cartItems.innerHTML += `
                <div class="cart-item">
                    <span>${item.name}</span>
                    <span>₹${item.price} x ${item.quantity}</span>
                </div>
            `;
        });
        
        cartTotal.textContent = total;
        cartCount.textContent = cart.reduce((sum, item) => sum + item.quantity, 0);
    }
}

// Checkout
if (checkoutBtn) {
    checkoutBtn.addEventListener('click', () => {
        if (cart.length === 0) {
            alert('Your cart is empty!');
            return;
        }
        alert(`Order placed! Total: ₹${total}`);
        cart = [];
        updateCart();
        if (cartPopup) {
            cartPopup.classList.remove("active");
        }
    });
}



// Profile page specific JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Profile menu navigation - Only on profile page
    const menuLinks = document.querySelectorAll('.profile-sidebar .profile-menu a');
    const sections = document.querySelectorAll('.profile-content > div');
    
    if (menuLinks.length > 0 && sections.length > 0) {
        menuLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                
                // Remove active class from all links
                menuLinks.forEach(l => l.classList.remove('active'));
                // Add active class to clicked link
                this.classList.add('active');
                
                // Hide all sections
                sections.forEach(section => section.style.display = 'none');
                
                // Show target section
                const targetId = this.getAttribute('href').substring(1);
                const targetSection = document.getElementById(targetId);
                if (targetSection) {
                    targetSection.style.display = 'block';
                }
            });
        });

        // Initialize - show personal info by default
        const personalSection = document.getElementById('personal');
        const ordersSection = document.getElementById('orders');
        
        if (personalSection) {
            personalSection.style.display = 'block';
        }
        if (ordersSection) {
            ordersSection.style.display = 'none';
        }
    }
});

// Emergency loader hide - if still stuck after 5 seconds
setTimeout(function() {
    const loader = document.getElementById("loader");
    if (loader && loader.style.display !== "none") {
        loader.style.display = "none";
        console.log("Loader force-hidden after timeout");
    }
}, 5000);
















document.addEventListener('DOMContentLoaded', function () {

    // ================= FORM TOGGLE =================
    const toggleButtons = document.querySelectorAll('.toggle-btn');
    const formSections = document.querySelectorAll('.form-section');

    toggleButtons.forEach(button => {
        button.addEventListener('click', function () {
            const formType = this.getAttribute('data-form');

            toggleButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');

            formSections.forEach(section => section.classList.remove('active'));
            document.getElementById(`${formType}-form`).classList.add('active');
        });
    });

    // ================= LOGIN FORM =================
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", function (e) {
            e.preventDefault();

            const email = document.getElementById("loginEmail").value.trim();
            const password = document.getElementById("loginPassword").value.trim();

            if (!email || !password) {
                showToast("Please fill all fields ❌", "error");
                return;
            }

            loginAPI(email, password);
        });
    }

    // ================= REGISTER FORM =================
    const registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", function (e) {
            e.preventDefault();

            const name = document.getElementById("registerName").value.trim();
            const email = document.getElementById("registerEmail").value.trim();
            const password = document.getElementById("registerPassword").value.trim();
            const confirmPassword = document.getElementById("registerConfirmPassword").value.trim();

            if (!name || !email || !password || !confirmPassword) {
                showToast("Please fill all fields ❌", "error");
                return;
            }

            if (password !== confirmPassword) {
                showToast("Passwords do not match ❌", "error");
                return;
            }

            const terms = document.getElementById("acceptTerms");
            if (terms && !terms.checked) {
                showToast("Accept Terms & Conditions ❌", "error");
                return;
            }

            registerAPI(name, email, password);
        });
    }
});

// ================= CUSTOM TOAST =================
function showToast(message, type = "info") {

    let toast = document.getElementById("customToast");

    if (!toast) {
        toast = document.createElement("div");
        toast.id = "customToast";

        toast.style.position = "fixed";
        toast.style.bottom = "30px";
        toast.style.right = "30px";
        toast.style.padding = "12px 20px";
        toast.style.borderRadius = "8px";
        toast.style.fontSize = "14px";
        toast.style.color = "#fff";
        toast.style.zIndex = "9999";
        toast.style.opacity = "0";
        toast.style.transition = "0.3s ease";

        document.body.appendChild(toast);
    }

    if (type === "success") toast.style.background = "#28a745";
    else if (type === "error") toast.style.background = "#dc3545";
    else toast.style.background = "#333";

    toast.innerText = message;
    toast.style.opacity = "1";

    setTimeout(() => { toast.style.opacity = "0"; }, 3000);
}

// ================= LOGIN API =================
async function loginAPI(email, password) {

    showToast("Please wait... Logging in 🔄", "info");

    try {
        const response = await fetch("https://ecommerce-2-i0yq.onrender.com/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const text = await response.text();
        console.log("Login Raw Response:", text);

        let data;
        try {
            data = JSON.parse(text);
        } catch {
            data = { message: text };
        }

        if (response.ok) {

            localStorage.setItem("userToken", data.token || "");
            localStorage.setItem("userEmail", email);
            localStorage.setItem("userRole", data.role || "USER");

            showToast("Login Successful ✅", "success");

            setTimeout(() => {
                if (data.role === "ADMIN")
                    window.location.href = "admin-dashboard.html";
                else
                    window.location.href = "index.html";
            }, 1500);

        } else {
            showToast(data.message || "Login Failed ❌", "error");
        }

    } catch (error) {
        console.error("Login Error:", error);
        showToast("Server error while login ❌", "error");
    }
}

// ================= REGISTER API =================
async function registerAPI(name, email, password) {

    showToast("Please wait... Creating account 🔄", "info");

    try {
        const response = await fetch("https://ecommerce-2-i0yq.onrender.com/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                name,
                email,
                password,
                role: "USER"
            })
        });

        const text = await response.text();
        console.log("Register Raw Response:", text);

        let data;
        try {
            data = JSON.parse(text);
        } catch {
            data = { message: text };
        }

        if (response.ok) {
            showToast("Account Created ✅ Now Login", "success");

            setTimeout(() => {
                showForm("login");
            }, 1500);
        } else {
            showToast(data.message || "Registration Failed ❌", "error");
        }

    } catch (error) {
        console.error("Register Error:", error);
        showToast("Server error while register ❌", "error");
    }
}

// ================= SHOW FORM =================
function showForm(formType) {
    const toggleButtons = document.querySelectorAll('.toggle-btn');
    const formSections = document.querySelectorAll('.form-section');

    toggleButtons.forEach(btn => {
        btn.classList.remove('active');
        if (btn.getAttribute("data-form") === formType) {
            btn.classList.add('active');
        }
    });

    formSections.forEach(section => section.classList.remove('active'));
    document.getElementById(`${formType}-form`).classList.add('active');
}

// ================= PASSWORD TOGGLE =================
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const icon = input.nextElementSibling.querySelector("i");

    if (input.type === "password") {
        input.type = "text";
        icon.classList.replace("fa-eye", "fa-eye-slash");
    } else {
        input.type = "password";
        icon.classList.replace("fa-eye-slash", "fa-eye");
    }
}

// ================= GOOGLE LOGIN (FUTURE) =================
function signInWithGoogle() {
    showToast("Google login coming soon 🚀", "info");
}
    
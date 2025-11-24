/**************** GLOBAL API ****************/
const BASE_API = "https://corsproxy.io/?https://ecommerce-2-i0yq.onrender.com";

/**************** LOADER ****************/
window.addEventListener('load', () => {
    const loader = document.getElementById("loader");
    if (loader) setTimeout(() => loader.style.display = "none", 1000);
});

setTimeout(() => {
    const loader = document.getElementById("loader");
    if (loader) loader.style.display = "none";
}, 5000);


/**************** TOAST ****************/
function showToast(message, type = "info") {
    let toast = document.getElementById("customToast");

    if (!toast) {
        toast = document.createElement("div");
        toast.id = "customToast";
        toast.style.position = "fixed";
        toast.style.bottom = "20px";
        toast.style.right = "20px";
        toast.style.padding = "12px 18px";
        toast.style.borderRadius = "8px";
        toast.style.color = "#fff";
        toast.style.zIndex = "9999";
        toast.style.fontSize = "14px";
        document.body.appendChild(toast);
    }

    toast.style.background = type === "success" ? "#28a745" :
                             type === "error" ? "#dc3545" : "#333";

    toast.innerText = message;
    toast.style.opacity = "1";

    setTimeout(() => toast.style.opacity = "0", 3000);
}


/**************** LOGIN ****************/
async function loginAPI(email, password) {

    showToast("Logging in... 🔄");

    try {
        const res = await fetch(BASE_API + "/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const data = await res.json();

        if (res.ok) {
            localStorage.setItem("userEmail", data.email);
            localStorage.setItem("userRole", data.role);
            localStorage.setItem("isLoggedIn", "true");

            console.log("✅ Login Success:", data);

            showToast("Login Successful ✅", "success");

            setTimeout(() => {
                window.location.href = data.role === "ADMIN"
                    ? "admin-dashboard.html"
                    : "profile.html";
            }, 1000);

        } else {
            console.error("❌ Login Failed:", data);
            showToast(data.message || "Login failed ❌", "error");
        }

    } catch (err) {
        console.error("Login Error:", err);
        showToast("Server error ❌", "error");
    }
}


/**************** REGISTER ****************/
async function registerAPI(name, email, password) {

    showToast("Registering...");

    try {
        const res = await fetch(BASE_API + "/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, password, role: "USER" })
        });

        const text = await res.text();

        if (res.ok) {
            showToast("Registered ✅ Now Login", "success");
        } else {
            showToast(text, "error");
        }

    } catch (err) {
        console.error("Register Error:", err);
        showToast("Server error ❌", "error");
    }
}


/**************** FORM HANDLING ****************/
document.addEventListener("DOMContentLoaded", () => {

    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");

    if (loginForm) {
        loginForm.addEventListener("submit", e => {
            e.preventDefault();
            loginAPI(
                document.getElementById("loginEmail").value,
                document.getElementById("loginPassword").value
            );
        });
    }

    if (registerForm) {
        registerForm.addEventListener("submit", e => {
            e.preventDefault();
            registerAPI(
                document.getElementById("registerName").value,
                document.getElementById("registerEmail").value,
                document.getElementById("registerPassword").value
            );
        });
    }
});


/**************** PROFILE LOAD ****************/
document.addEventListener("DOMContentLoaded", async () => {

    const email = localStorage.getItem("userEmail");

    if (!email) {
        showToast("Login required ❌", "error");
        window.location.href = "sign.html";
        return;
    }

    try {
        const response = await fetch(BASE_API + `/api/profile/${email}`);
        const user = await response.json();

        console.log("✅ Profile loaded:", user);

        if (!response.ok) {
            showToast("Profile load failed ❌", "error");
            return;
        }

        document.querySelector(".profile-name").innerText =
            user.firstName + " " + user.lastName;

        document.querySelector(".profile-email").innerText = user.email;

        document.getElementById("firstName").value = user.firstName || "";
        document.getElementById("lastName").value = user.lastName || "";
        document.getElementById("phone").value = user.phone || "";
        document.getElementById("birthdate").value = user.birthdate || "";

        if (user.profileImage) {
            document.querySelector(".profile-picture").src = user.profileImage;
        }

    } catch (err) {
        console.error("Profile Load Error:", err);
        showToast("Profile load error ❌", "error");
    }
});


/**************** UPDATE PROFILE ****************/
const profileForm = document.querySelector("#personal form");

if (profileForm) {
    profileForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = localStorage.getItem("userEmail");

        const data = {
            firstName: document.getElementById("firstName").value,
            lastName: document.getElementById("lastName").value,
            phone: document.getElementById("phone").value,
            birthdate: document.getElementById("birthdate").value,
            profileImage: document.querySelector(".profile-picture").src
        };

        try {

            const response = await fetch(
                BASE_API + `/api/profile/update/${email}`,
                {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data)
                }
            );

            const result = await response.text();

            if (response.ok) {
                console.log("✅ Profile updated:", result);

                document.querySelector(".profile-name").innerText =
                    data.firstName + " " + data.lastName;

                showToast("Profile updated ✅", "success");
            } else {
                console.error("Update error:", result);
                showToast("Update failed ❌", "error");
            }

        } catch (err) {
            console.error("Update Error:", err);
            showToast("Server error while updating ❌", "error");
        }
    });
}


/**************** IMAGE PREVIEW ****************/
const profilePic = document.querySelector(".profile-picture");

if (profilePic) {
    profilePic.addEventListener("click", () => {

        const input = document.createElement("input");
        input.type = "file";
        input.accept = "image/*";

        input.onchange = () => {
            const file = input.files[0];
            const reader = new FileReader();

            reader.onload = () => {
                profilePic.src = reader.result;
                showToast("Preview updated ✅ Now click Update Profile");
            };

            reader.readAsDataURL(file);
        };

        input.click();
    });
}


/**************** LOGOUT ****************/
const logoutBtn = document.querySelector("a[href='#logout']");
if (logoutBtn) {
    logoutBtn.addEventListener("click", e => {
        e.preventDefault();
        localStorage.clear();
        showToast("Logged out ✅", "success");
        setTimeout(() => window.location.href = "sign.html", 1000);
    });
}

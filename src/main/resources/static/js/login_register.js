document.addEventListener('DOMContentLoaded', () => {
    const loginBtn = document.getElementById('signin-btn-gradient');
    const logoutBtn = document.getElementById('logout-btn-gradient');

    // Đảm bảo các phần tử tồn tại
    if (!loginBtn || !logoutBtn) {
        console.error('Login or Logout button not found in DOM.');
        return;
    }

    // Hàm lấy giá trị cookie theo tên
    const getCookie = (name) => {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    };

    // Kiểm tra token trong cookie
    const token = getCookie('token');

    if (token) {
        // Nếu token tồn tại, ẩn nút Login và hiển thị nút Logout
        loginBtn.classList.add('d-none');
        logoutBtn.classList.remove('d-none');

        // Xử lý sự kiện Logout
        logoutBtn.addEventListener('click', () => {
            // Xóa token trong cookie
            document.cookie = 'token=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
            // Chuyển hướng về trang đăng nhập
            window.location.href = '/auth/login';
        });
    } else {
        // Nếu token không tồn tại, ẩn nút Logout và hiển thị nút Login
        loginBtn.classList.remove('d-none');
        logoutBtn.classList.add('d-none');
    }
});

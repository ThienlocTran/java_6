const host = "https://java6-6f9c1-default-rtdb.asia-southeast1.firebasedatabase.app";

const $api = {
    key: null, // Biến này để nhớ xem bạn đang chọn sửa sinh viên nào

    // Hàm GET: Lấy dữ liệu từ các ô input trên màn hình gom thành 1 object
    get student() {
        return {
            id: $("#id").val(),
            name: $("#name").val(),
            mark: parseFloat($("#mark").val()), // Ép kiểu về số
            gender: $("#male").prop("checked")  // Trả về true nếu chọn Male
        }
    },

    // Hàm SET: Bắn dữ liệu từ object lên các ô input trên màn hình
    set student(e) {
        $("#id").val(e.id);
        $("#name").val(e.name);
        $("#mark").val(e.mark);
        $("#male").prop("checked", e.gender);
        $("#female").prop("checked", !e.gender);
    },

    // Đổ danh sách sinh viên từ Firebase ra bảng HTML
    fillToTable() {
        var url = `${host}/students.json`;
        axios.get(url).then(resp => {
            $("tbody").empty(); // Xóa sạch dữ liệu cũ trong bảng
            if (resp.data) {
                // Duyệt qua từng dòng dữ liệu Firebase trả về
                Object.keys(resp.data).forEach(key => {
                    var e = resp.data[key];
                    var tr = `<tr>
                        <td>${e.id}</td>
                        <td>${e.name}</td>
                        <td>${e.mark}</td>
                        <td>${e.gender ? 'Male' : 'Female'}</td>
                        <td>
                            <button onclick="$api.edit('${key}')">Edit</button>
                            <button onclick="$api.delete('${key}')">Delete</button>
                        </td>
                    </tr>`;
                    $("tbody").append(tr); // Gắn dòng mới vào bảng
                });
            }
        }).catch(error => {
            console.log("Error", error);
            alert("Lỗi tải danh sách sinh viên!");
        });
    },

    // Khi bấm nút Edit trên 1 dòng
    edit(key) {
        this.key = key; // Lưu lại cái key đang sửa
        var url = `${host}/students/${key}.json`;
        axios.get(url).then(resp => {
            this.student = resp.data; // Gọi hàm set student() ở trên để hiển thị lên form
        }).catch(error => {
            console.log("Error", error);
            alert("Lỗi tải sinh viên!");
        });
    },

    // Khi bấm nút Create
    create() {
        var url = `${host}/students.json`;
        axios.post(url, this.student).then(resp => {
            this.fillToTable(); // Cập nhật lại bảng
            this.reset();       // Xóa trắng form
        }).catch(error => {
            console.log("Error", error);
            alert("Lỗi thêm sinh viên mới!");
        });
    },

    // Khi bấm nút Update
    update() {
        if (!this.key) {
            alert("Vui lòng chọn sinh viên cần sửa (Bấm nút Edit) trước!");
            return;
        }
        var url = `${host}/students/${this.key}.json`;
        axios.put(url, this.student).then(resp => {
            this.fillToTable();
            this.reset();
        }).catch(error => {
            console.log("Error", error);
            alert("Lỗi cập nhật sinh viên!");
        });
    },

    // Khi bấm nút Delete
    delete(key) {
        var deleteKey = key || this.key; // Ưu tiên lấy key truyền vào, không thì lấy key đang chọn trên form
        if (!deleteKey) return;

        var url = `${host}/students/${deleteKey}.json`;
        axios.delete(url).then(resp => {
            this.fillToTable();
            this.reset();
        }).catch(error => {
            console.log("Error", error);
            alert("Lỗi xóa sinh viên!");
        });
    },

    // Xóa trắng các ô nhập liệu
    reset() {
        this.student = {id: "", name: "", mark: "", gender: true};
        this.key = null;
    }
}

// Khởi động: Tải dữ liệu lần đầu tiên khi trang web vừa bật lên
$api.fillToTable();
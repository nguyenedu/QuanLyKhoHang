import java.util.Scanner;

class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;

    public Product() {
    }

    public Product(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void showInfo() {
        double total = price * quantity;
        System.out.printf("[ID: %s| Tên: %s| Giá: %.2f| SL: %d| Tổng: %.2f]\n",
                id, name, price, quantity, total);
    }
}

public class quanLyKhoHang {
    static Product[] products = new Product[100];
    static int count = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("==================MENU=================");
            System.out.println("1. Thêm sản phẩm");
            System.out.println("2. Hiển thị và thống kê");
            System.out.println("3. Tìm kiếm sản phẩm rẻ nhất");
            System.out.println("4. Cập nhật số lượng(sửa)");
            System.out.println("5. Xóa sản phẩm");
            System.out.println("6. Sắp xếp sản phẩm theo giá giảm dần");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn của bạn: ");
            choice = sc.nextInt();
            sc.nextLine();
            System.out.println("======================================");

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    displayProduct();
                    double totalValue = calculateTotalValue();
                    System.out.printf("Tổng giá trị kho hàng: %.2f\n", totalValue);
                    break;
                case 3:
                    findCheapestProduct();
                    break;
                case 4:
                    updateProductQuantity();
                    break;
                case 5:
                    deleteProduct();
                    break;
                case 6:
                    sortProductsByPriceDesc();
                    displayProduct();
                    break;
                case 0:
                    System.out.println("Đã thoát chương trình");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ");
            }
        } while (choice != 0);
    }

    static String formatName(String name) {
        name = name.trim();
        name = name.replaceAll("\\s+", " ");
        String[] words = name.split(" ");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return formattedName.toString().trim();
    }

    static boolean isExist(String id) {
        for (int i = 0; i < count; i++) {
            if (products[i].getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    static void addProduct() {
        if (count >= 100) {
            System.out.println("Kho hàng đã đầy, không thể thêm sản phẩm mới.");
            return;
        }

        String id;
        do {
            System.out.print("Nhập mã sản phẩm: ");
            id = sc.nextLine();
            if (isExist(id)) {
                System.out.println("Mã sản phẩm đã tồn tại. Vui lòng nhập mã khác.");
            }
        } while (isExist(id));

        System.out.print("Nhập tên sản phẩm: ");
        String name = formatName(sc.nextLine());

        System.out.print("Nhập giá sản phẩm: ");
        double price = sc.nextDouble();

        System.out.print("Nhập số lượng sản phẩm: ");
        int quantity = sc.nextInt();
        sc.nextLine();

        products[count++] = new Product(id, name, price, quantity);
        System.out.println("Thêm sản phẩm thành công");
    }

    static void displayProduct() {
        if (count == 0) {
            System.out.println("Kho hàng trống.");
            return;
        }
        for (int i = 0; i < count; i++) {
            products[i].showInfo();
        }
    }

    static double calculateTotalValue() {
        double totalValue = 0;
        for (int i = 0; i < count; i++) {
            totalValue += products[i].getPrice() * products[i].getQuantity();
        }
        return totalValue;
    }

    static void findCheapestProduct() {
        if (count == 0) {
            System.out.println("Kho hàng trống.");
            return;
        }
        Product cheapest = products[0];
        for (int i = 1; i < count; i++) {
            if (products[i].getPrice() < cheapest.getPrice()) {
                cheapest = products[i];
            }
        }
        System.out.println("Sản phẩm rẻ nhất:");
        System.out.printf("Mã SP: %s, Tên SP: %s, Giá: %.2f, Số lượng: %d\n",
                cheapest.getId(),
                cheapest.getName(),
                cheapest.getPrice(),
                cheapest.getQuantity());
    }

    static void updateProductQuantity() {
        System.out.print("Nhập mã sản phẩm cần cập nhật: ");
        String id = sc.nextLine();

        for (int i = 0; i < count; i++) {
            if (products[i].getId().equalsIgnoreCase(id)) {
                System.out.print("Nhập số lượng mới: ");
                int newQuantity = sc.nextInt();
                products[i].setQuantity(newQuantity);
                System.out.println("Cập nhật số lượng thành công");

                System.out.print("Nhập giá mới của sản phẩm: ");
                double newPrice = sc.nextDouble();
                sc.nextLine();
                products[i].setPrice(newPrice);
                System.out.println("Cập nhật giá thành công!");
                return;
            }
        }
        System.out.println("Sản phẩm không tồn tại.");
    }

    static void deleteProduct() {
        System.out.print("Nhập mã sản phẩm cần xóa: ");
        String id = sc.nextLine();

        for (int i = 0; i < count; i++) {
            if (products[i].getId().equalsIgnoreCase(id)) {
                for (int j = i; j < count - 1; j++) {
                    products[j] = products[j + 1];
                }
                count--;
                System.out.println("Xóa sản phẩm thành công!");
                return;
            }
        }
        System.out.println("Không tìm thấy sản phẩm cần xóa.");
    }

    static void sortProductsByPriceDesc() {
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (products[i].getPrice() < products[j].getPrice()) {
                    Product temp = products[i];
                    products[i] = products[j];
                    products[j] = temp;
                }
            }
        }
        System.out.println("Sản phẩm đã được sắp xếp theo giá giảm dần.");
    }
}
import java.util.Scanner;

// Lớp Product
class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = formatName(name);
        this.price = price;
        this.quantity = quantity;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = formatName(name);
    }

    // Override equals() - So sánh theo id và name (không phân biệt hoa thường)
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Product)) {
            return false;
        }

        Product other = (Product) obj;

        return this.id == other.id &&
                this.name.equalsIgnoreCase(other.name);
    }

    // Override toString() - Sử dụng StringBuilder
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id)
                .append(" | Name: ").append(name)
                .append(" | Price: ").append(price)
                .append(" | Quantity: ").append(quantity);
        return sb.toString();
    }

    public String formatName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        String[] words = trimmed.split("\\s+");

        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1));
                }

                if (i < words.length - 1) {
                    result.append(" ");
                }
            }
        }

        return result.toString();
    }
}

// Lớp quản lý kho hàng
class WarehouseManager {
    private static final int MAX_CATEGORIES = 5;
    private static final int MAX_PRODUCTS_PER_CATEGORY = 20;

    private Product[][] warehouse;  // Mảng 2 chiều: 5 danh mục x 20 sản phẩm
    private int[] counts;           // Số lượng sản phẩm trong mỗi danh mục
    private String[] categoryNames; // Tên các danh mục

    public WarehouseManager() {
        warehouse = new Product[MAX_CATEGORIES][MAX_PRODUCTS_PER_CATEGORY];
        counts = new int[MAX_CATEGORIES];
        categoryNames = new String[]{"Điện tử", "Gia dụng", "Thực phẩm", "Thời trang", "Khác"};
    }

    // CHỨC NĂNG 1: Thêm sản phẩm vào kho
    public void addProduct(int categoryId, Product product) {
        StringBuilder message = new StringBuilder();

        if (categoryId < 0 || categoryId >= MAX_CATEGORIES) {
            message.append("Danh mục không hợp lệ");
            System.out.println(message);
            return;
        }

        if (counts[categoryId] >= MAX_PRODUCTS_PER_CATEGORY) {
            message.append("Danh mục đã đầy, không thể thêm sản phẩm");
            System.out.println(message);
            return;
        }

        for (int i = 0; i < counts[categoryId]; i++) {
            if (warehouse[categoryId][i].equals(product)) {
                message.append("Sản phẩm đã tồn tại trong danh mục");
                System.out.println(message);
                return;
            }
        }

        warehouse[categoryId][counts[categoryId]] = product;
        counts[categoryId]++;

        message.append("Đã thêm sản phẩm ")
                .append(product.getName())
                .append(" vào danh mục ")
                .append(categoryId);
        System.out.println(message);
    }

    // CHỨC NĂNG 2: Tìm kiếm sản phẩm theo tên
    public void searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Từ khóa tìm kiếm không hợp lệ");
            return;
        }

        StringBuilder result = new StringBuilder();
        boolean found = false;
        String lowerKeyword = keyword.toLowerCase();

        for (int i = 0; i < MAX_CATEGORIES; i++) {
            for (int j = 0; j < counts[i]; j++) {
                Product p = warehouse[i][j];
                if (p.getName().toLowerCase().contains(lowerKeyword)) {
                    result.append(p.toString()).append("\n");
                    found = true;
                }
            }
        }

        if (found) {
            System.out.println("Kết quả tìm kiếm:");
            System.out.print(result);
        } else {
            System.out.println("Không tìm thấy sản phẩm phù hợp");
        }
    }

    // CHỨC NĂNG 3: Xóa sản phẩm theo ID
    public void deleteProductById(int id) {
        boolean found = false;

        // Tìm sản phẩm trong toàn bộ kho
        for (int i = 0; i < MAX_CATEGORIES; i++) {
            for (int j = 0; j < counts[i]; j++) {
                if (warehouse[i][j].getId() == id) {
                    // Tìm thấy sản phẩm, thực hiện dịch mảng
                    shiftArray(i, j);
                    counts[i]--;
                    found = true;
                    System.out.println("Đã xóa sản phẩm có ID: " + id);
                    return;
                }
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy sản phẩm cần xóa");
        }
    }

    // Hàm dịch mảng sau khi xóa phần tử
    private void shiftArray(int categoryId, int position) {
        // Dịch các phần tử từ position+1 về trái
        for (int i = position; i < counts[categoryId] - 1; i++) {
            warehouse[categoryId][i] = warehouse[categoryId][i + 1];
        }
        // Xóa phần tử cuối
        warehouse[categoryId][counts[categoryId] - 1] = null;
    }

    // CHỨC NĂNG 4: Sắp xếp toàn bộ kho theo giá giảm dần
    public void sortByPriceDescending() {
        int totalProducts = 0;
        for (int i = 0; i < MAX_CATEGORIES; i++) {
            totalProducts += counts[i];
        }

        if (totalProducts == 0) {
            System.out.println("Kho hàng trống");
            return;
        }

        Product[] allProducts = new Product[totalProducts];
        int index = 0;
        for (int i = 0; i < MAX_CATEGORIES; i++) {
            for (int j = 0; j < counts[i]; j++) {
                allProducts[index++] = warehouse[i][j];
            }
        }

        quickSort(allProducts, 0, allProducts.length - 1);

        System.out.println("Danh sách sản phẩm sau khi sắp xếp theo giá giảm dần:");
        for (Product p : allProducts) {
            System.out.println(p);
        }
    }

    private void quickSort(Product[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private int partition(Product[] arr, int low, int high) {
        double pivot = arr[high].getPrice();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].getPrice() > pivot) {
                i++;
                Product temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        Product temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    // CHỨC NĂNG 5: Thống kê dạng ma trận
    public void generateStatistics() {
        System.out.println("=== THỐNG KÊ KHO HÀNG ===");
        System.out.println("Danh mục | Tổng giá trị");
        System.out.println("----------------------------");

        double[] totalValues = new double[MAX_CATEGORIES];
        int maxIndex = 0;
        double maxValue = 0;

        for (int i = 0; i < MAX_CATEGORIES; i++) {
            double total = 0;
            for (int j = 0; j < counts[i]; j++) {
                Product p = warehouse[i][j];
                total += p.getPrice() * p.getQuantity();
            }
            totalValues[i] = total;

            if (total > maxValue) {
                maxValue = total;
                maxIndex = i;
            }

            System.out.printf("%d (%s) | %,.0f", i, categoryNames[i], total);
        }

        System.out.println("----------------------------");
        System.out.println("Danh mục có giá trị lớn nhất: " + maxIndex + " (" + categoryNames[maxIndex] + ")");
        System.out.printf("Giá trị: %,.0f", maxValue);
    }

    public void displayAllProducts() {
        System.out.println("=== DANH SÁCH TẤT CẢ SẢN PHẨM ===");
        boolean isEmpty = true;

        for (int i = 0; i < MAX_CATEGORIES; i++) {
            if (counts[i] > 0) {
                isEmpty = false;
                System.out.println("Danh mục " + i + " - " + categoryNames[i] + ":");
                for (int j = 0; j < counts[i]; j++) {
                    System.out.println("  " + warehouse[i][j]);
                }
            }
        }

        if (isEmpty) {
            System.out.println("Kho hàng trống");
        }
    }
}

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static WarehouseManager manager = new WarehouseManager();

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            System.out.print("Chọn chức năng: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addProductMenu();
                    break;
                case 2:
                    searchProductMenu();
                    break;
                case 3:
                    deleteProductMenu();
                    break;
                case 4:
                    manager.sortByPriceDescending();
                    break;
                case 5:
                    manager.generateStatistics();
                    break;
                case 6:
                    manager.displayAllProducts();
                    break;
                case 0:
                    System.out.println("Thoát chương trình");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ");
            }

            System.out.println();
        }
    }

    private static void displayMenu() {
        System.out.println("========================================");
        System.out.println("   HỆ THỐNG QUẢN LÝ KHO HÀNG");
        System.out.println("========================================");
        System.out.println("1. Thêm sản phẩm vào kho");
        System.out.println("2. Tìm kiếm sản phẩm theo tên");
        System.out.println("3. Xóa sản phẩm theo ID");
        System.out.println("4. Sắp xếp toàn bộ kho theo giá giảm dần");
        System.out.println("5. Thống kê dạng ma trận");
        System.out.println("6. Hiển thị tất cả sản phẩm");
        System.out.println("0. Thoát");
        System.out.println("========================================");
    }

    private static void addProductMenu() {
        System.out.println("--- THÊM SẢN PHẨM ---");
        System.out.println("Danh mục: 0-Điện tử, 1-Gia dụng, 2-Thực phẩm, 3-Thời trang, 4-Khác");

        System.out.print("Nhập mã danh mục: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nhập ID sản phẩm: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nhập tên sản phẩm: ");
        String name = scanner.nextLine();

        System.out.print("Nhập giá sản phẩm: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Nhập số lượng: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product(id, name, price, quantity);
        manager.addProduct(categoryId, product);
    }

    private static void searchProductMenu() {
        System.out.println("--- TÌM KIẾM SẢN PHẨM ---");
        System.out.print("Nhập từ khóa tìm kiếm: ");
        String keyword = scanner.nextLine();
        manager.searchByName(keyword);
    }

    private static void deleteProductMenu() {
        System.out.println("--- XÓA SẢN PHẨM ---");
        System.out.print("Nhập ID sản phẩm cần xóa: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        manager.deleteProductById(id);
    }
}
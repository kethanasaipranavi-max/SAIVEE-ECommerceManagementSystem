package com.ecommerce.ui;

import com.ecommerce.model.Customer;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductFrame extends JFrame {

    private final List<Product> products;
    private final ProductService productService;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final Customer customer;
    private final CartService cartService;
    private final InventoryService inventoryService;
    private final OrderProcessingService orderProcessingService;
    private final WishlistService wishlistService;
    private final RecentlyViewedService recentlyViewedService;
    private final RecommendationService recommendationService;
    private final RatingService ratingService;
    private final ReviewService reviewService;

    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> subcategoryComboBox;
    private JComboBox<String> brandComboBox;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JComboBox<String> sortComboBox;
    private JPanel productPanel;

    private static final Color NAVY = new Color(13,45,82);
    private static final Color DARK_BLUE = new Color(25,70,125);
    private static final Color ROYAL_BLUE = new Color(40,90,180);
    private static final Color BLUE = new Color(45,120,210);
    private static final Color SKY_BLUE = new Color(80,160,230);
    private static final Color LIGHT_BLUE = new Color(220,240,255);
    private static final Color PALE_BLUE = new Color(240,248,255);

    public ProductFrame(
            List<Product> products,
            ProductService productService,
            OrderService orderService,
            NotificationService notificationService,
            Customer customer,
            CartService cartService,
            InventoryService inventoryService,
            OrderProcessingService orderProcessingService) {

        this.products = products == null ? new ArrayList<>() : products;
        this.productService = productService;
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.customer = customer;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
        this.orderProcessingService = orderProcessingService;
        this.wishlistService = WishlistService.getInstance();
        this.recentlyViewedService = RecentlyViewedService.getInstance();
        this.recommendationService = new RecommendationService(productService, orderService);
        this.ratingService = RatingService.getInstance();
        this.reviewService = ReviewService.getInstance();

        setTitle("SAIVEE - Shopping Catalogue");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createHeader();
        createSearchPanel();
        createProductArea();
        createBottomPanel();
        refreshProducts(new ArrayList<>(this.products));
    }

    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(15,25,15,25));

        JLabel logo = new JLabel("SAIVEE");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel title = new JLabel(
                "Marketplace Catalogue  •  Category → Subcategory → Brand → Product");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        header.add(logo, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
    }

    private void createSearchPanel() {
        JPanel outer = new JPanel(new BorderLayout(8,8));
        outer.setBackground(PALE_BLUE);
        outer.setBorder(new EmptyBorder(8,10,8,10));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        row1.setOpaque(false);
        searchField = new JTextField(22);
        JButton search = createButton("SEARCH", BLUE);
        search.addActionListener(e -> applyFilters());
        searchField.addActionListener(e -> applyFilters());
        row1.add(new JLabel("Search:"));
        row1.add(searchField);
        row1.add(search);

        JPanel filters = new JPanel();
        filters.setOpaque(false);
        filters.setLayout(new BoxLayout(filters, BoxLayout.Y_AXIS));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        row2.setOpaque(false);
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        row3.setOpaque(false);

        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("All Categories");
        products.stream().map(Product::getCategory)
                .filter(x -> x != null && !x.isBlank()).distinct().sorted()
                .forEach(categoryComboBox::addItem);

        subcategoryComboBox = new JComboBox<>();
        subcategoryComboBox.addItem("All Subcategories");

        brandComboBox = new JComboBox<>();
        brandComboBox.addItem("All Brands");

        categoryComboBox.addActionListener(e -> refreshHierarchyChoices());
        subcategoryComboBox.addActionListener(e -> refreshBrandChoices());

        minPriceField = new JTextField(7);
        maxPriceField = new JTextField(7);

        sortComboBox = new JComboBox<>(new String[]{
                "Default","Price Low-High","Price High-Low",
                "Name A-Z","Name Z-A","Rating High-Low",
                "Stock High-Low","Stock Low-High"
        });

        JButton apply = createButton("APPLY FILTERS", ROYAL_BLUE);
        JButton clear = createButton("CLEAR", SKY_BLUE);
        apply.addActionListener(e -> applyFilters());
        clear.addActionListener(e -> clearFilters());

        row2.add(new JLabel("Category:"));
        row2.add(categoryComboBox);
        row2.add(new JLabel("Subcategory:"));
        row2.add(subcategoryComboBox);
        row2.add(new JLabel("Brand:"));
        row2.add(brandComboBox);
        row2.add(new JLabel("Min ₹:"));
        row2.add(minPriceField);
        row2.add(new JLabel("Max ₹:"));
        row2.add(maxPriceField);
        row2.add(new JLabel("Sort:"));
        row2.add(sortComboBox);
        row3.add(apply);
        row3.add(clear);

        filters.add(row2);
        filters.add(row3);
        outer.add(row1, BorderLayout.NORTH);
        outer.add(filters, BorderLayout.CENTER);
        add(outer, BorderLayout.BEFORE_FIRST_LINE);
    }

    private void refreshHierarchyChoices() {
        String category = String.valueOf(categoryComboBox.getSelectedItem());
        subcategoryComboBox.removeAllItems();
        subcategoryComboBox.addItem("All Subcategories");
        products.stream()
                .filter(p -> "All Categories".equals(category)
                        || (p.getCategory() != null && p.getCategory().equalsIgnoreCase(category)))
                .map(Product::getSubcategory)
                .filter(x -> x != null && !x.isBlank())
                .distinct().sorted().forEach(subcategoryComboBox::addItem);
        refreshBrandChoices();
    }

    private void refreshBrandChoices() {
        String category = String.valueOf(categoryComboBox.getSelectedItem());
        String sub = String.valueOf(subcategoryComboBox.getSelectedItem());
        brandComboBox.removeAllItems();
        brandComboBox.addItem("All Brands");
        products.stream()
                .filter(p -> "All Categories".equals(category)
                        || (p.getCategory() != null && p.getCategory().equalsIgnoreCase(category)))
                .filter(p -> "All Subcategories".equals(sub)
                        || (p.getSubcategory() != null && p.getSubcategory().equalsIgnoreCase(sub)))
                .map(Product::getBrand)
                .filter(x -> x != null && !x.isBlank()).distinct().sorted()
                .forEach(brandComboBox::addItem);
    }

    private void createProductArea() {
        productPanel = new JPanel(new GridLayout(0,3,12,12));
        productPanel.setBackground(PALE_BLUE);
        productPanel.setBorder(new EmptyBorder(12,12,12,12));
        JScrollPane scroll = new JScrollPane(productPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void refreshProducts(List<Product> filtered) {
        productPanel.removeAll();
        productPanel.setLayout(new GridLayout(0,3,12,12));
        if (filtered == null || filtered.isEmpty()) {
            productPanel.setLayout(new BorderLayout());
            JLabel empty = new JLabel("No products found.", SwingConstants.CENTER);
            empty.setFont(new Font("Arial", Font.BOLD, 20));
            empty.setForeground(DARK_BLUE);
            productPanel.add(empty);
        } else {
            for (Product product : filtered) productPanel.add(createProductCard(product));
        }
        productPanel.revalidate();
        productPanel.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout(6,6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_BLUE,2),
                new EmptyBorder(10,10,10,10)));

        JLabel name = new JLabel(product.getProductName(), SwingConstants.CENTER);
        name.setForeground(NAVY);
        name.setFont(new Font("Arial",Font.BOLD,16));

        String rating = product.getRatingCount() == 0
                ? "Not rated yet" : String.format("⭐ %.1f (%d)",product.getAverageRating(),product.getRatingCount());

        JLabel details = new JLabel("<html><center>"
                + "Category: " + product.getCategory()
                + "<br>Subcategory: " + product.getSubcategory()
                + "<br>Brand: " + product.getBrand()
                + "<br>Price: ₹" + String.format("%.2f",product.getPrice())
                + "<br>Stock: " + product.getQuantity()
                + "<br>Variants: " + product.getVariants().size()
                + "<br>" + rating + "</center></html>", SwingConstants.CENTER);
        details.setForeground(DARK_BLUE);

        JPanel buttons = new JPanel(new GridLayout(2,2,5,5));
        buttons.setOpaque(false);

        JButton view = createButton("VIEW", NAVY);
        JButton cart = createButton("ADD TO CART", BLUE);
        JButton wish = createWishlistButton(product);
        JButton rate = createButton("RATE", ROYAL_BLUE);

        view.addActionListener(e -> viewProduct(product));
        cart.addActionListener(e -> addToCart(product));
        wish.addActionListener(e -> toggleWishlist(product,wish));
        rate.addActionListener(e -> rateProduct(product));

        buttons.add(view);
        buttons.add(cart);
        buttons.add(wish);
        buttons.add(rate);

        card.add(name,BorderLayout.NORTH);
        card.add(details,BorderLayout.CENTER);
        card.add(buttons,BorderLayout.SOUTH);
        return card;
    }

    private void viewProduct(Product product) {
        recentlyViewedService.record(customer, product);

        List<Product> recs = recommendationService.recommend(customer, product, 5);
        StringBuilder text = new StringBuilder();
        text.append("PRODUCT DETAILS\n\n")
                .append("Name: ").append(product.getProductName()).append("\n")
                .append("Category: ").append(product.getCategory()).append("\n")
                .append("Subcategory: ").append(product.getSubcategory()).append("\n")
                .append("Brand: ").append(product.getBrand()).append("\n")
                .append("Price: ₹").append(String.format("%.2f",product.getPrice())).append("\n")
                .append("Stock: ").append(product.getQuantity()).append("\n")
                .append("Variants: ").append(product.getVariants().isEmpty() ? "None" : product.getVariants()).append("\n")
                .append("Rating: ").append(product.getRatingCount()==0 ? "Not rated" :
                        String.format("%.1f / 5 (%d ratings)",product.getAverageRating(),product.getRatingCount()))
                .append("\n\nCUSTOMER REVIEWS\n");
        List<com.ecommerce.model.Review> reviews = reviewService.getProductReviews(product.getProductId());
        if (reviews.isEmpty()) text.append("No written reviews yet.\n");
        else for (com.ecommerce.model.Review r : reviews) text.append("★").append(r.getStars()).append(" - ").append(r.getCustomerName()).append(": ").append(r.getText()).append("\n").append(r.hasReply()?"  SAIVEE reply: "+r.getAdminReply()+"\n":"");
        text.append("\nRECOMMENDED FOR YOU\n");

        for (Product p : recs) {
            text.append("→ ").append(p.getBrand()).append(" ")
                    .append(p.getProductName()).append(" (₹")
                    .append(String.format("%.2f",p.getPrice())).append(")\n");
        }

        JTextArea area = new JTextArea(text.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced",Font.PLAIN,13));
        JScrollPane pane = new JScrollPane(area);
        pane.setPreferredSize(new Dimension(650,420));
        JOptionPane.showMessageDialog(this,pane,"SAIVEE - Product Details",JOptionPane.INFORMATION_MESSAGE);
    }

    private void rateProduct(Product product) {
        if (reviewService.hasReviewed(customer, product)) { JOptionPane.showMessageDialog(this,"You have already reviewed this product.","SAIVEE Reviews",JOptionPane.INFORMATION_MESSAGE); return; }
        boolean purchased = orderService.getCustomerOrders(customer).stream().anyMatch(o -> o.getStatus() == com.ecommerce.model.OrderStatus.DELIVERED && o.getOrderItems().stream().anyMatch(i -> i.getProduct()!=null && i.getProduct().getProductId()==product.getProductId()));
        if (!purchased) { JOptionPane.showMessageDialog(this,"Ratings and reviews are available after this product has been delivered to you.","SAIVEE Reviews",JOptionPane.INFORMATION_MESSAGE); return; }
        String input=JOptionPane.showInputDialog(this,"Rate " + product.getProductName() + " (1-5):","Product Rating",JOptionPane.QUESTION_MESSAGE); if(input==null)return;
        try { int stars=Integer.parseInt(input.trim()); if(stars<1||stars>5)throw new NumberFormatException(); String review=JOptionPane.showInputDialog(this,"Write a short review (optional):","Product Review",JOptionPane.QUESTION_MESSAGE); if(review==null)review=""; Review r=reviewService.addReview(customer,product,stars,review); if(r==null)throw new IllegalArgumentException(); JOptionPane.showMessageDialog(this,"Thank you! Your rating and review were recorded in the shopping catalogue. A confirmation email was sent.","SAIVEE Reviews",JOptionPane.INFORMATION_MESSAGE); applyFilters(); } catch(Exception ex){JOptionPane.showMessageDialog(this,"Please enter a whole number from 1 to 5.","Invalid Rating",JOptionPane.ERROR_MESSAGE);}
    }

    private JButton createWishlistButton(Product product) {
        return createButton(
                wishlistService.containsProduct(customer,product)
                        ? "♥ WISHLIST" : "♡ WISHLIST",
                wishlistService.containsProduct(customer,product) ? DARK_BLUE : ROYAL_BLUE);
    }

    private void toggleWishlist(Product product, JButton button) {
        if (wishlistService.containsProduct(customer,product)) {
            wishlistService.removeFromWishlist(customer,product);
            button.setText("♡ WISHLIST");
            button.setBackground(ROYAL_BLUE);
        } else {
            wishlistService.addToWishlist(customer,product);
            button.setText("♥ WISHLIST");
            button.setBackground(DARK_BLUE);
        }
    }

    private void addToCart(Product product) {
        recentlyViewedService.record(customer,product);
        if (product.getQuantity() <= 0) {
            JOptionPane.showMessageDialog(this,"This product is out of stock.","SAIVEE",JOptionPane.WARNING_MESSAGE);
            return;
        }
        String variant = null;
        if (!product.getVariants().isEmpty()) {
            Object selected = JOptionPane.showInputDialog(this, "Select variant:",
                    "SAIVEE - Product Variant", JOptionPane.QUESTION_MESSAGE, null,
                    product.getVariants().toArray(), product.getVariants().get(0));
            if (selected == null) return;
            variant = ((com.ecommerce.model.ProductVariant) selected).getKey();
        }
        String input = JOptionPane.showInputDialog(this,"Enter quantity:",
                "Add to Cart",JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        try {
            int quantity = Integer.parseInt(input.trim());
            int available = variant == null ? product.getQuantity() : product.getVariantStock(variant);
            if (quantity <= 0 || quantity > available) throw new NumberFormatException();
            boolean added = cartService.addToCart(customer,product,quantity,variant);
            if (!added) throw new NumberFormatException();
            JOptionPane.showMessageDialog(this,quantity+" × "+product.getProductName()
                    + (variant == null ? "" : " (" + variant + ")") + " added to your cart.",
                    "SAIVEE Cart",JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"Enter a valid quantity within available stock.",
                    "Invalid Quantity",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        String search = searchField.getText().trim().toLowerCase();
        String category = String.valueOf(categoryComboBox.getSelectedItem());
        String subcategory = String.valueOf(subcategoryComboBox.getSelectedItem());
        String brand = String.valueOf(brandComboBox.getSelectedItem());
        Double min = parsePrice(minPriceField.getText());
        Double max = parsePrice(maxPriceField.getText());

        if ((!minPriceField.getText().isBlank() && min == null)
                || (!maxPriceField.getText().isBlank() && max == null)
                || (min != null && max != null && min > max)) {
            JOptionPane.showMessageDialog(this,"Please enter a valid price range.",
                    "Invalid Filter",JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Product> result = products.stream()
                .filter(p -> search.isEmpty()
                        || contains(p.getProductName(),search)
                        || contains(p.getBrand(),search)
                        || contains(p.getCategory(),search)
                        || contains(p.getSubcategory(),search))
                .filter(p -> "All Categories".equals(category)
                        || p.getCategory().equalsIgnoreCase(category))
                .filter(p -> "All Subcategories".equals(subcategory)
                        || p.getSubcategory().equalsIgnoreCase(subcategory))
                .filter(p -> "All Brands".equals(brand)
                        || p.getBrand().equalsIgnoreCase(brand))
                .filter(p -> min == null || p.getPrice() >= min)
                .filter(p -> max == null || p.getPrice() <= max)
                .collect(Collectors.toList());

        String sort = String.valueOf(sortComboBox.getSelectedItem());
        switch (sort) {
            case "Price Low-High" -> result.sort(Comparator.comparingDouble(Product::getPrice));
            case "Price High-Low" -> result.sort(Comparator.comparingDouble(Product::getPrice).reversed());
            case "Name A-Z" -> result.sort(Comparator.comparing(Product::getProductName,String.CASE_INSENSITIVE_ORDER));
            case "Name Z-A" -> result.sort(Comparator.comparing(Product::getProductName,String.CASE_INSENSITIVE_ORDER).reversed());
            case "Rating High-Low" -> result.sort(Comparator.comparingDouble(Product::getAverageRating).reversed());
            case "Stock High-Low" -> result.sort(Comparator.comparingInt(Product::getQuantity).reversed());
            case "Stock Low-High" -> result.sort(Comparator.comparingInt(Product::getQuantity));
        }
        refreshProducts(result);
    }

    private boolean contains(String value,String search) {
        return value != null && value.toLowerCase().contains(search);
    }

    private Double parsePrice(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            double d = Double.parseDouble(value.trim());
            return d >= 0 ? d : null;
        } catch (NumberFormatException e) { return null; }
    }

    private void clearFilters() {
        searchField.setText("");
        categoryComboBox.setSelectedIndex(0);
        subcategoryComboBox.setSelectedIndex(0);
        brandComboBox.setSelectedIndex(0);
        minPriceField.setText("");
        maxPriceField.setText("");
        sortComboBox.setSelectedIndex(0);
        refreshProducts(new ArrayList<>(products));
    }

    private void createBottomPanel() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER,8,8));
        bottom.setBackground(LIGHT_BLUE);

        JButton recent = createButton("RECENTLY VIEWED", NAVY);
        JButton recommend = createButton("RECOMMENDED FOR YOU", ROYAL_BLUE);
        JButton wish = createButton("MY WISHLIST", BLUE);
        JButton cart = createButton("MY CART", SKY_BLUE);
        JButton close = createButton("DASHBOARD", DARK_BLUE);

        recent.addActionListener(e -> showProductList("RECENTLY VIEWED",
                recentlyViewedService.getRecentlyViewed(customer)));
        recommend.addActionListener(e -> showProductList("RECOMMENDED FOR YOU",
                recommendationService.recommend(customer,null,8)));
        wish.addActionListener(e -> showProductList("MY WISHLIST",
                wishlistService.getWishlist(customer)));
        cart.addActionListener(e -> openCart());
        close.addActionListener(e -> dispose());

        bottom.add(recent); bottom.add(recommend); bottom.add(wish); bottom.add(cart); bottom.add(close);
        add(bottom,BorderLayout.SOUTH);
    }

    private void showProductList(String title,List<Product> list) {
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this,"No products available.",
                    "SAIVEE - "+title,JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder b = new StringBuilder();
        for (Product p : list) {
            b.append("• ").append(p.getBrand()).append(" - ").append(p.getProductName())
                    .append(" | ").append(p.getCategory()).append(" / ").append(p.getSubcategory())
                    .append(" | ₹").append(String.format("%.2f",p.getPrice())).append("\n");
        }
        JTextArea area = new JTextArea(b.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced",Font.PLAIN,13));
        JScrollPane pane = new JScrollPane(area);
        pane.setPreferredSize(new Dimension(850,450));
        JOptionPane.showMessageDialog(this,pane,"SAIVEE - "+title,JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWishlist() {
        showProductList("MY WISHLIST",wishlistService.getWishlist(customer));
    }

    private void openCart() {
        new CartFrame(productService,orderService,notificationService,customer,
                cartService,inventoryService,orderProcessingService).setVisible(true);
    }

    private JButton createButton(String text,Color color) {
        JButton b = new JButton(text);
        b.setBackground(color); b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial",Font.BOLD,11));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE,1),
                new EmptyBorder(7,10,7,10)));
        return b;
    }
}

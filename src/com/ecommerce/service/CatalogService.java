package com.ecommerce.service;

import com.ecommerce.model.Product;

/**
 * SAIVEE catalogue seed data.
 * Structure: Main Category -> Subcategory -> Brand -> Products.
 */
public final class CatalogService {

    private static int nextId = 1000;

    private CatalogService() {}

    public static void seedCatalog(ProductService productService) {
        if (productService == null || !productService.getProducts().isEmpty()) return;

        add(productService, "Women Fashion", "Sarees",
                new String[]{"Libas","Biba","Aurelia"},
                new String[]{"Cotton Printed Saree","Embroidered Saree"}, 699, 3499);
        add(productService, "Women Fashion", "Kurtis",
                new String[]{"Libas","Biba","W","Aurelia"},
                new String[]{"Printed Kurti","Anarkali Kurti"}, 499, 2499);
        add(productService, "Women Fashion", "Dresses",
                new String[]{"ONLY","AND","Tokyo Talkies"},
                new String[]{"Floral Dress","Party Wear Dress"}, 799, 2999);
        add(productService, "Women Fashion", "Tops",
                new String[]{"ONLY","AND","H&M"},
                new String[]{"Casual Top","Printed Top"}, 499, 1999);
        add(productService, "Women Fashion", "Jeans",
                new String[]{"Levi's","ONLY","Roadster"},
                new String[]{"Slim Fit Jeans","Straight Fit Jeans"}, 999, 2999);
        add(productService, "Women Fashion", "Leggings",
                new String[]{"Jockey","Go Colors","Zivame"},
                new String[]{"Cotton Leggings","Stretch Leggings"}, 299, 999);
        add(productService, "Women Fashion", "Ethnic Wear",
                new String[]{"Biba","Libas","W"},
                new String[]{"Ethnic Set","Kurta Palazzo Set"}, 999, 3999);
        add(productService, "Women Fashion", "Nightwear",
                new String[]{"Clovia","Zivame","Jockey"},
                new String[]{"Cotton Night Suit","Printed Night Dress"}, 499, 1499);

        add(productService, "Men Fashion", "T-Shirts",
                new String[]{"Levi's","Puma","Roadster"},
                new String[]{"Cotton T-Shirt","Polo T-Shirt"}, 399, 1999);
        add(productService, "Men Fashion", "Shirts",
                new String[]{"Peter England","Van Heusen","Allen Solly"},
                new String[]{"Formal Shirt","Casual Checked Shirt"}, 699, 2499);
        add(productService, "Men Fashion", "Jeans",
                new String[]{"Levi's","Wrangler","Lee"},
                new String[]{"Slim Fit Jeans","Regular Fit Jeans"}, 999, 3499);
        add(productService, "Men Fashion", "Trousers",
                new String[]{"Peter England","Van Heusen","Louis Philippe"},
                new String[]{"Formal Trousers","Slim Fit Trousers"}, 799, 2999);
        add(productService, "Men Fashion", "Kurtas",
                new String[]{"Manyavar","FabIndia","Raymond"},
                new String[]{"Cotton Kurta","Festive Kurta"}, 699, 2499);
        add(productService, "Men Fashion", "Ethnic Wear",
                new String[]{"Manyavar","FabIndia","Biba"},
                new String[]{"Kurta Pajama Set","Nehru Jacket Set"}, 1299, 4999);
        add(productService, "Men Fashion", "Innerwear",
                new String[]{"Jockey","Van Heusen","Amul Macho"},
                new String[]{"Cotton Vest","Cotton Briefs"}, 199, 799);
        add(productService, "Men Fashion", "Jackets",
                new String[]{"Puma","Levi's","Roadster"},
                new String[]{"Denim Jacket","Winter Jacket"}, 999, 3999);

        add(productService, "Kids & Baby", "Girls Clothing",
                new String[]{"Mothercare","Allen Solly Junior","Pantaloons"},
                new String[]{"Girls Party Dress","Girls Cotton Frock"}, 499, 2499);
        add(productService, "Kids & Baby", "Boys Clothing",
                new String[]{"Allen Solly Junior","U.S. Polo Assn. Kids","Pantaloons"},
                new String[]{"Boys Printed T-Shirt","Boys Casual Shirt"}, 399, 1999);
        add(productService, "Kids & Baby", "Baby Clothing",
                new String[]{"Mothercare","R for Rabbit","Babyhug"},
                new String[]{"Baby Romper","Baby Cotton Set"}, 299, 1499);
        add(productService, "Kids & Baby", "Toys",
                new String[]{"Funskool","Hamleys","Fisher-Price"},
                new String[]{"Educational Toy","Building Blocks Set"}, 299, 2499);
        add(productService, "Kids & Baby", "Baby Care",
                new String[]{"Johnson's Baby","Mamaearth","Sebamed"},
                new String[]{"Baby Lotion","Baby Shampoo"}, 199, 999);
        add(productService, "Kids & Baby", "School Bags",
                new String[]{"Skybags","American Tourister","Wildcraft"},
                new String[]{"Kids School Bag","Character School Bag"}, 499, 1999);

        add(productService, "Footwear", "Women Footwear",
                new String[]{"Bata","Metro","Mochi"},
                new String[]{"Women Flats","Women Heels"}, 499, 2499);
        add(productService, "Footwear", "Men Footwear",
                new String[]{"Bata","Red Tape","Clarks"},
                new String[]{"Formal Shoes","Casual Loafers"}, 799, 3999);
        add(productService, "Footwear", "Kids Footwear",
                new String[]{"Bata","Skechers","Campus"},
                new String[]{"Kids Sneakers","Kids School Shoes"}, 399, 1999);
        add(productService, "Footwear", "Sports Shoes",
                new String[]{"Nike","Adidas","Puma"},
                new String[]{"Running Shoes","Training Shoes"}, 1499, 7999);
        add(productService, "Footwear", "Sandals",
                new String[]{"Sparx","Bata","Crocs"},
                new String[]{"Casual Sandals","Comfort Sandals"}, 399, 2499);
        add(productService, "Footwear", "Slippers",
                new String[]{"Paragon","Sparx","Bata"},
                new String[]{"Daily Slippers","Comfort Flip Flops"}, 199, 999);

        add(productService, "Jewellery & Accessories", "Earrings",
                new String[]{"Mia","Sukkhi","Voylla"},
                new String[]{"Stud Earrings","Drop Earrings"}, 199, 1999);
        add(productService, "Jewellery & Accessories", "Necklaces",
                new String[]{"Mia","Sukkhi","Voylla"},
                new String[]{"Pendant Necklace","Layered Necklace"}, 299, 2999);
        add(productService, "Jewellery & Accessories", "Bangles",
                new String[]{"Sukkhi","Voylla","Accessorize"},
                new String[]{"Gold Tone Bangles","Fashion Bangle Set"}, 199, 1499);
        add(productService, "Jewellery & Accessories", "Rings",
                new String[]{"Mia","Sukkhi","Voylla"},
                new String[]{"Adjustable Ring","Statement Ring"}, 199, 1999);
        add(productService, "Jewellery & Accessories", "Watches",
                new String[]{"Fastrack","Titan","Sonata"},
                new String[]{"Analog Watch","Casual Watch"}, 799, 4999);
        add(productService, "Jewellery & Accessories", "Sunglasses",
                new String[]{"Fastrack","Ray-Ban","Vogue"},
                new String[]{"Aviator Sunglasses","Wayfarer Sunglasses"}, 599, 6999);
        add(productService, "Jewellery & Accessories", "Belts",
                new String[]{"Levi's","Van Heusen","Allen Solly"},
                new String[]{"Leather Belt","Casual Belt"}, 399, 1499);
        add(productService, "Jewellery & Accessories", "Wallets",
                new String[]{"WildHorn","Tommy Hilfiger","Fossil"},
                new String[]{"Leather Wallet","Card Holder Wallet"}, 399, 2999);

        add(productService, "Bags & Luggage", "Handbags",
                new String[]{"Lavie","Baggit","Caprese"},
                new String[]{"Tote Handbag","Structured Handbag"}, 799, 3999);
        add(productService, "Bags & Luggage", "Sling Bags",
                new String[]{"Lavie","Baggit","Caprese"},
                new String[]{"Casual Sling Bag","Party Sling Bag"}, 499, 2499);
        add(productService, "Bags & Luggage", "Backpacks",
                new String[]{"Wildcraft","American Tourister","Skybags"},
                new String[]{"Laptop Backpack","Travel Backpack"}, 699, 2999);
        add(productService, "Bags & Luggage", "Laptop Bags",
                new String[]{"American Tourister","Samsonite","Wildcraft"},
                new String[]{"Office Laptop Bag","Padded Laptop Bag"}, 899, 4999);
        add(productService, "Bags & Luggage", "Wallets",
                new String[]{"WildHorn","Fossil","Tommy Hilfiger"},
                new String[]{"Slim Wallet","Leather Wallet"}, 399, 2999);
        add(productService, "Bags & Luggage", "Trolley Bags",
                new String[]{"Safari","VIP","American Tourister"},
                new String[]{"Cabin Trolley","Large Trolley"}, 1999, 7999);

        add(productService, "Beauty & Personal Care", "Makeup",
                new String[]{"Maybelline","Lakme","Nykaa Cosmetics"},
                new String[]{"Matte Lipstick","Liquid Foundation"}, 199, 1499);
        add(productService, "Beauty & Personal Care", "Skincare",
                new String[]{"Cetaphil","Minimalist","Mamaearth"},
                new String[]{"Face Cleanser","Vitamin C Serum"}, 299, 1499);
        add(productService, "Beauty & Personal Care", "Haircare",
                new String[]{"L'Oreal","Tresemme","Mamaearth"},
                new String[]{"Shampoo","Hair Serum"}, 199, 999);
        add(productService, "Beauty & Personal Care", "Bath & Body",
                new String[]{"Dove","Nivea","The Body Shop"},
                new String[]{"Body Wash","Body Lotion"}, 199, 1299);
        add(productService, "Beauty & Personal Care", "Grooming",
                new String[]{"Philips","Gillette","Beardo"},
                new String[]{"Beard Trimmer","Grooming Kit"}, 499, 2999);
        add(productService, "Beauty & Personal Care", "Fragrances",
                new String[]{"Engage","Fogg","Park Avenue"},
                new String[]{"Eau De Parfum","Body Spray"}, 199, 1999);

        add(productService, "Home & Living", "Home Decor",
                new String[]{"Home Centre","Chumbak","IKEA"},
                new String[]{"Decorative Vase","Wall Decor Set"}, 299, 2999);
        add(productService, "Home & Living", "Bedsheets",
                new String[]{"Spaces","Raymond Home","Portico"},
                new String[]{"Cotton Bedsheet","Printed Bedsheet"}, 599, 2499);
        add(productService, "Home & Living", "Curtains",
                new String[]{"D'Decor","Story@Home","Home Centre"},
                new String[]{"Blackout Curtains","Printed Curtains"}, 699, 2999);
        add(productService, "Home & Living", "Cushions",
                new String[]{"Home Centre","Spaces","IKEA"},
                new String[]{"Decorative Cushion","Printed Cushion Set"}, 299, 1499);
        add(productService, "Home & Living", "Storage",
                new String[]{"Nilkamal","IKEA","Home Centre"},
                new String[]{"Storage Box","Drawer Organizer"}, 199, 1999);
        add(productService, "Home & Living", "Cleaning",
                new String[]{"Scotch-Brite","Gala","Milton"},
                new String[]{"Floor Cleaning Set","Cleaning Brush Set"}, 149, 999);
        add(productService, "Home & Living", "Bathroom",
                new String[]{"Milton","Cello","Home Centre"},
                new String[]{"Bathroom Organizer","Bath Mat"}, 199, 1299);

        add(productService, "Kitchen & Appliances", "Cookware",
                new String[]{"Prestige","Hawkins","Wonderchef"},
                new String[]{"Non Stick Fry Pan","Pressure Cooker"}, 699, 3999);
        add(productService, "Kitchen & Appliances", "Kitchen Tools",
                new String[]{"Milton","Cello","Pigeon"},
                new String[]{"Knife Set","Kitchen Tool Set"}, 199, 999);
        add(productService, "Kitchen & Appliances", "Storage Containers",
                new String[]{"Tupperware","Cello","Milton"},
                new String[]{"Airtight Container Set","Food Storage Set"}, 299, 1499);
        add(productService, "Kitchen & Appliances", "Appliances",
                new String[]{"Philips","Prestige","Pigeon"},
                new String[]{"Mixer Grinder","Electric Kettle"}, 799, 4999);
        add(productService, "Kitchen & Appliances", "Dinnerware",
                new String[]{"Corelle","Cello","Borosil"},
                new String[]{"Dinner Set","Glassware Set"}, 499, 2999);

        add(productService, "Electronics", "Earphones",
                new String[]{"boAt","JBL","Boult"},
                new String[]{"Wireless Earbuds","Neckband Earphones"}, 499, 2999);
        add(productService, "Electronics", "Headphones",
                new String[]{"Sony","JBL","boAt"},
                new String[]{"Wireless Headphones","Noise Cancelling Headphones"}, 999, 7999);
        add(productService, "Electronics", "Speakers",
                new String[]{"JBL","Sony","boAt"},
                new String[]{"Bluetooth Speaker","Portable Party Speaker"}, 799, 9999);
        add(productService, "Electronics", "Chargers",
                new String[]{"Anker","Belkin","Samsung"},
                new String[]{"Fast Charger","USB-C Charger"}, 399, 2499);
        add(productService, "Electronics", "Cables",
                new String[]{"Anker","Belkin","boAt"},
                new String[]{"USB-C Cable","Lightning Cable"}, 199, 1499);
        add(productService, "Electronics", "Power Banks",
                new String[]{"Mi","Ambrane","Portronics"},
                new String[]{"10000mAh Power Bank","20000mAh Power Bank"}, 699, 2499);
        add(productService, "Electronics", "Mobile Accessories",
                new String[]{"Spigen","Ringke","Portronics"},
                new String[]{"Mobile Cover","Phone Stand"}, 199, 1499);

        add(productService, "Watches", "Analog",
                new String[]{"Titan","Fastrack","Casio"},
                new String[]{"Classic Analog Watch","Leather Strap Watch"}, 999, 6999);
        add(productService, "Watches", "Digital",
                new String[]{"Casio","Fastrack","Timex"},
                new String[]{"Digital Sports Watch","Digital Casual Watch"}, 799, 4999);
        add(productService, "Watches", "Smart Watches",
                new String[]{"Noise","boAt","Fire-Boltt"},
                new String[]{"Bluetooth Smart Watch","Fitness Smart Watch"}, 999, 4999);
        add(productService, "Watches", "Sports Watches",
                new String[]{"Casio","Fastrack","Timex"},
                new String[]{"Outdoor Sports Watch","Digital Sports Watch"}, 999, 5999);

        add(productService, "Sports & Fitness", "Gym Equipment",
                new String[]{"Decathlon","Strauss","Boldfit"},
                new String[]{"Adjustable Dumbbells","Resistance Kit"}, 499, 4999);
        add(productService, "Sports & Fitness", "Yoga",
                new String[]{"Decathlon","Boldfit","Strauss"},
                new String[]{"Yoga Mat","Yoga Block Set"}, 299, 1999);
        add(productService, "Sports & Fitness", "Cricket",
                new String[]{"SG","SS","MRF"},
                new String[]{"Cricket Bat","Cricket Kit"}, 799, 6999);
        add(productService, "Sports & Fitness", "Football",
                new String[]{"Nivia","Adidas","Nike"},
                new String[]{"Football","Football Training Kit"}, 399, 3999);
        add(productService, "Sports & Fitness", "Badminton",
                new String[]{"Yonex","Li-Ning","Apacs"},
                new String[]{"Badminton Racket","Badminton Kit"}, 699, 4999);
        add(productService, "Sports & Fitness", "Cycling",
                new String[]{"Firefox","Hero","Decathlon"},
                new String[]{"Mountain Bicycle","Cycling Helmet"}, 999, 14999);
        add(productService, "Sports & Fitness", "Fitness Accessories",
                new String[]{"Boldfit","Strauss","Decathlon"},
                new String[]{"Skipping Rope","Gym Gloves"}, 199, 999);

        add(productService, "Automotive", "Car Accessories",
                new String[]{"3M","Kia","Hyundai"},
                new String[]{"Car Seat Cover","Car Floor Mat"}, 499, 4999);
        add(productService, "Automotive", "Bike Accessories",
                new String[]{"Mototrance","ViaTerra","Raida"},
                new String[]{"Bike Saddle Bag","Bike Phone Mount"}, 399, 2999);
        add(productService, "Automotive", "Car Care",
                new String[]{"3M","Turtle Wax","Wavex"},
                new String[]{"Car Shampoo","Dashboard Polish"}, 199, 1499);
        add(productService, "Automotive", "Helmets",
                new String[]{"Steelbird","Vega","Studds"},
                new String[]{"Full Face Helmet","Open Face Helmet"}, 699, 3999);
        add(productService, "Automotive", "Mobile Holders",
                new String[]{"Portronics","Spigen","Tygot"},
                new String[]{"Dashboard Mobile Holder","Bike Mobile Holder"}, 199, 999);

        add(productService, "Books & Stationery", "Books",
                new String[]{"Penguin","HarperCollins","Rupa"},
                new String[]{"Fiction Book","Self Help Book"}, 199, 999);
        add(productService, "Books & Stationery", "Notebooks",
                new String[]{"Classmate","Navneet","Paperkraft"},
                new String[]{"Spiral Notebook","Hardbound Notebook"}, 99, 499);
        add(productService, "Books & Stationery", "Pens",
                new String[]{"Parker","Reynolds","Cello"},
                new String[]{"Ball Pen Set","Gel Pen Set"}, 49, 999);
        add(productService, "Books & Stationery", "School Supplies",
                new String[]{"Classmate","Camlin","DOMS"},
                new String[]{"School Stationery Kit","Geometry Box"}, 99, 699);
        add(productService, "Books & Stationery", "Office Supplies",
                new String[]{"Kangaro","Oddy","Classmate"},
                new String[]{"Stapler Set","Desk Organizer"}, 99, 999);
        add(productService, "Books & Stationery", "Art & Craft",
                new String[]{"Camlin","Faber-Castell","DOMS"},
                new String[]{"Colour Pencil Set","Sketch Pen Set"}, 99, 999);

        add(productService, "Grocery & Food", "Snacks",
                new String[]{"Haldiram's","Bikaji","Balaji"},
                new String[]{"Namkeen Pack","Chips Combo"}, 99, 499);
        add(productService, "Grocery & Food", "Tea & Coffee",
                new String[]{"Tata Tea","Bru","Nescafe"},
                new String[]{"Tea Powder","Instant Coffee"}, 149, 999);
        add(productService, "Grocery & Food", "Spices",
                new String[]{"MDH","Everest","Catch"},
                new String[]{"Garam Masala","Kitchen Spice Combo"}, 99, 499);
        add(productService, "Grocery & Food", "Dry Fruits",
                new String[]{"Happilo","Farmley","Nutraj"},
                new String[]{"Premium Almonds","Cashew Pack"}, 299, 1499);
        add(productService, "Grocery & Food", "Chocolates",
                new String[]{"Cadbury","Nestle","Ferrero Rocher"},
                new String[]{"Milk Chocolate","Chocolate Gift Box"}, 99, 999);
        add(productService, "Grocery & Food", "Pickles",
                new String[]{"Priya","Mother's Recipe","Bedekar"},
                new String[]{"Mango Pickle","Mixed Pickle"}, 99, 499);

        add(productService, "Pet Supplies", "Pet Food",
                new String[]{"Pedigree","Royal Canin","Whiskas"},
                new String[]{"Adult Pet Food","Puppy Food"}, 299, 2499);
        add(productService, "Pet Supplies", "Toys",
                new String[]{"Kong","Trixie","HUFT"},
                new String[]{"Chew Toy","Interactive Pet Toy"}, 199, 1499);
        add(productService, "Pet Supplies", "Bowls",
                new String[]{"Trixie","HUFT","PetSafe"},
                new String[]{"Steel Pet Bowl","Elevated Pet Bowl"}, 199, 999);
        add(productService, "Pet Supplies", "Grooming",
                new String[]{"Himalaya Pet Care","Trixie","Wahl"},
                new String[]{"Pet Shampoo","Pet Grooming Brush"}, 199, 1299);
        add(productService, "Pet Supplies", "Collars & Leashes",
                new String[]{"HUFT","Trixie","PetSafe"},
                new String[]{"Adjustable Collar","Pet Leash Set"}, 199, 999);
    }

    private static void add(ProductService service, String category,
                            String subcategory, String[] brands,
                            String[] productNames, double min, double max) {
        for (String brand : brands) {
            for (String baseName : productNames) {
                double price = min + (nextId * 37 % Math.max(1, (int)(max - min + 1)));
                price = Math.round(Math.min(max, price) * 100.0) / 100.0;
                int stock = 8 + (nextId % 28);
                Product p = new Product(nextId++, baseName, category,
                        subcategory, price, stock, brand);
                service.addProduct(p);
            }
        }
    }
}


function showLoginForm()
{
    templateBuilder.build('login-form', {}, 'login');
}

function hideModalForm()
{
    templateBuilder.clear('login');
}

function login()
{
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    userService.login(username, password);
    hideModalForm()
}

function showImageDetailForm(product, imageUrl)
{
    const imageDetail = {
        name: product,
        imageUrl: imageUrl
    };

    templateBuilder.build('image-detail',imageDetail,'login')
}

function loadHome()
{
    templateBuilder.build('home',{},'main')

    productService.search();
    categoryService.getAllCategories(loadCategories);
}

function editProfile()
{
    profileService.loadProfile();
}

function saveProfile()
{
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const phone = document.getElementById("phone").value;
    const email = document.getElementById("email").value;
    const address = document.getElementById("address").value;
    const city = document.getElementById("city").value;
    const state = document.getElementById("state").value;
    const zip = document.getElementById("zip").value;

    const profile = {
        firstName,
        lastName,
        phone,
        email,
        address,
        city,
        state,
        zip
    };

    profileService.updateProfile(profile);
}

function showCart()
{
    cartService.loadCartPage();
}

function clearCart()
{
    cartService.clearCart();
    cartService.loadCartPage();
}

function setCategory(control)
{
    productService.addCategoryFilter(control.value);
    productService.search();

}

function setColor(control)
{
    productService.addColorFilter(control.value);
    productService.search();

}

function setMinPrice(control)
{
    // const slider = document.getElementById("min-price");
    const label = document.getElementById("min-price-display")
    label.innerText = control.value;

    const value = control.value != 0 ? control.value : "";
    productService.addMinPriceFilter(value)
    productService.search();

}

function setMaxPrice(control)
{
    // const slider = document.getElementById("min-price");
    const label = document.getElementById("max-price-display")
    label.innerText = control.value;

    const value = control.value != 1500 ? control.value : "";
    productService.addMaxPriceFilter(value)
    productService.search();

}

function closeError(control)
{
    setTimeout(() => {
        control.click();
    },3000);
}

function addToCart(button) {
    // Get the product image
    const productCard = button.closest('.product');
    const productImg = productCard.querySelector('.photo img');
    
    // Get cart icon position - targeting the cart element itself
    const cart = document.querySelector('.cart'); // Target the entire cart container
    const cartRect = cart.getBoundingClientRect();
    
    // Create image clone
    const imgClone = productImg.cloneNode(true);
    const startRect = productImg.getBoundingClientRect();
    
    // Style the cloned image
    imgClone.style.position = 'fixed';
    imgClone.style.top = startRect.top + 'px';
    imgClone.style.left = startRect.left + 'px';
    imgClone.style.width = startRect.width + 'px';
    imgClone.style.height = startRect.height + 'px';
    imgClone.style.opacity = '0.75';
    imgClone.style.pointerEvents = 'none';
    imgClone.style.zIndex = '1000';
    imgClone.classList.add('cart-animation');
    
    // Add clone to body
    document.body.appendChild(imgClone);
    
    // Start animation in next frame with adjusted position
    requestAnimationFrame(() => {
        imgClone.style.transform = 'scale(0.25)';
        imgClone.style.top = (cartRect.top + window.scrollY) + 'px';
        imgClone.style.left = (cartRect.left) + 'px'; // Removed the width/2 adjustment
        imgClone.style.opacity = '0';
    });
    
    // Clean up and add cart shake effect
    setTimeout(() => {
        imgClone.remove();
        cart.classList.add('cart-shake');
        setTimeout(() => cart.classList.remove('cart-shake'), 500);
    }, 500);
}

document.addEventListener('DOMContentLoaded', () => {

    loadHome();
});

function setupPage(page) {
    if (page === 'products') {
        // Only add filter box for products page
        main.innerHTML = `
            <div class="filter-box">
                <!-- filter content -->
            </div>
            <div id="content" class="content-form">
                <!-- product content -->
            </div>
        `;
    } else if (page === 'cart') {
        // Cart page without filter box
        main.innerHTML = `
            <div id="content" class="content-form">
                <!-- cart content -->
            </div>
        `;
    }
}

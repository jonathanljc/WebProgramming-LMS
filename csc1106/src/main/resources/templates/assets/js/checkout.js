// Add product to cart
function addProductToCart(productName, price, imageUrl) {
    var cartList = document.getElementById('cartList');
    var listItem = document.createElement('li');
    listItem.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');
    listItem.innerHTML = `
        <div class="media">
            <img src="${imageUrl}" class="mr-3" alt="${productName}" width="50">
            <div class="media-body">
                <h6 class="my-0">${productName}</h6>
                <small class="text-muted">Brief description</small>
            </div>
        </div>
        <span class="text-muted">$${price.toFixed(2)}</span>
        <button type="button" class="btn btn-sm btn-danger" onclick="removeItem(this)">Remove</button>
    `;
    cartList.appendChild(listItem);
    updateCartItemCount();
}

// Update cart item count
function updateCartItemCount() {
    var cartList = document.getElementById('cartList');
    var itemCount = cartList.getElementsByTagName('li').length;
    document.getElementById('cartItemCount').textContent = itemCount;
}

// Remove item from cart
function removeItem(button) {
    var listItem = button.parentElement;
    listItem.remove();
    updateCartItemCount();
}

// Example starter JavaScript for disabling form submissions if there are invalid fields
(function() {
    'use strict';

    window.addEventListener('load', function() {
        // Fetch all the forms we want to apply custom Bootstrap validation styles to
        var forms = document.getElementsByClassName('needs-validation');

        // Loop over them and prevent submission
        var validation = Array.prototype.filter.call(forms, function(form) {
            form.addEventListener('submit', function(event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();

// Sample usage to test adding products to the cart
window.addEventListener('DOMContentLoaded', (event) => {
    addProductToCart('Product 1', 10.00, 'https://via.placeholder.com/100');
    addProductToCart('Product 2', 20.00, 'https://via.placeholder.com/100');
    addProductToCart('Product 3', 30.00, 'https://via.placeholder.com/100');
});
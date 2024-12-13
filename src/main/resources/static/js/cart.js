$(document).ready(function () {
    $('.quantity').on('input', function () {
        updateCartItem($(this));
    });

    function updateCartItem(quantityInput) {
        let quantity = quantityInput.val();
        let chassisNumber = quantityInput.closest('.cart-item').data('chassis-number');
        let priceElement = quantityInput.closest('.cart-item').find('.price');
        let totalElement = quantityInput.closest('.cart-item').find('.totalPrice');

        $.ajax({
            url: '/cart/updateCart/' + chassisNumber + '/' + quantity,
            type: 'POST',
            success: function (response) {
                let price = response.price;
                let total = price * quantity;

                priceElement.text(price.toFixed(2));  // Update price with formatted value
                totalElement.text(total.toFixed(2));  // Update total with formatted value

                updateCartTotal();  // Update cart total after updating item
            }
        });
    }

    function updateCartTotal() {
        let totalPrice = 0;
        let totalQuantity = 0;

        $('.cart-item').each(function () {
            let price = parseFloat($(this).find('.price').text());
            let quantity = parseInt($(this).find('.quantity').val());
            let total = price * quantity;

            $(this).find('.totalPrice').text(total.toFixed(2));  // Update total for each item
            totalPrice += total;
            totalQuantity += quantity;
        });

        $('#totalQuantity').text(totalQuantity);  // Update total quantity
        $('#finalTotalPrice').text(totalPrice.toFixed(2));  // Update final total price
    }

    const currentDate = new Date();
    const expectedDeliveryDate = new Date(currentDate);
    expectedDeliveryDate.setDate(currentDate.getDate() + 7);

    const formattedCurrentDate = formatDate(currentDate);
    const formattedExpectedDeliveryDate = formatDate(expectedDeliveryDate);

    document.getElementById('currentDate').textContent = formattedCurrentDate;
    document.getElementById('expectedDeliveryDate').textContent = formattedExpectedDeliveryDate;

    function formatDate(date) {
        const day = date.getDate();
        const month = date.getMonth() + 1;
        const year = date.getFullYear();
        return (day < 10 ? '0' : '') + day + '.' + (month < 10 ? '0' : '') + month + '.' + year;
    }
});

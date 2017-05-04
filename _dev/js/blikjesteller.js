FastClick.attach(document.body);

Vue.filter('currency', currencyFilter);

var vm = new Vue({
    el: '#blikjesteller',
    data: {
        blikjes: null,
        totalAmount: 0,
        stateChanged: false
    },
    methods: {
        plusOne: function(blikje) {
            blikje.amount += 1;
            updateTotal();
        },
        minusOne: function(blikje) {
            if (blikje.amount > 0) {
                blikje.amount -= 1;
                updateTotal();
            }
        }
    }
});

vm.$http.get('/global/config/blikjes.json').then(function(response) {
    vm.blikjes = response.data;
});

function updateTotal() {
    vm.stateChanged = true;
    var totalAmount = 0;

    vm.blikjes.forEach(function(blikje) {
        totalAmount += (blikje.price * blikje.amount);
    });

    vm.totalAmount = totalAmount;
}

function currencyFilter(value, currency, decimals) {
    var digitsRE = /(\d{3})(?=\d)/g
    value = parseFloat(value)
    if (!isFinite(value) || (!value && value !== 0)) return ''
    currency = currency != null ? currency : '$'
    decimals = decimals != null ? decimals : 2
    var stringified = Math.abs(value).toFixed(decimals)
    stringified = stringified.replace('.', ',');
    var _int = decimals
        ? stringified.slice(0, -1 - decimals)
        : stringified
    var i = _int.length % 3
    var head = i > 0
        ? (_int.slice(0, i) + (_int.length > 3 ? '.' : ''))
        : ''
    var _float = decimals
        ? stringified.slice(-1 - decimals)
        : ''
    var sign = value < 0 ? '-' : ''
    return sign + currency + head +
        _int.slice(i).replace(digitsRE, '$1,') +
        _float
}

window.onbeforeunload = function (e) {
    if (vm.stateChanged) {
        return "Ingevulde gegevens gaan verloren.";
    } else {
        return undefined;
    }
};
var coopSocket = null;
var vm = new Vue({
    el: '#blikjesteller',
    filters: {
        currency: currencyFilter
    },
    data: {
        blikjes: null,
        totalAmount: 0,
        stateChanged: false,
        joinCoopId: null,
        coopStarted: false
    },
    methods: {
        plusOne: function(blikje) {
            if (coopSocket != null) {
                coopPlusOne(blikje);
            } else {
                blikje.amount += 1;
                updateTotal();
            }
        },
        minusOne: function(blikje) {
            if (blikje.amount > 0) {
                if (coopSocket != null) {
                    coopMinusOne(blikje);
                } else {
                    blikje.amount -= 1;
                    updateTotal();
                }
            }
        },
        startCoop: function() {
            startCoop(vm.coopId);
        },
        joinCoop: function() {
            joinCoop(vm.joinCoopId);
        }
    },
    computed: {
        coopId: function () {
            var text = "";
            var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

            for (var i = 0; i < 4; i++) {
              text += possible.charAt(Math.floor(Math.random() * possible.length));
            }

            return text;
        }
    }
});

vm.$http.get('/global/config/blikjes.json').then(function(response) {
    vm.blikjes = response.data;
});

FastClick.attach(document.body);

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

function startCoop(id) {
    if (coopSocket != null) {
        coopSocket.close();
    }
    vm.$http.post('/service/coop/start?id=' + id, vm.blikjes).then(function(response) {
        startCoopWebsocket(id)

        vm.coopStarted = true;
        document.querySelector('dialog').close();
    });
}

function joinCoop(id) {
    if (coopSocket != null) {
        coopSocket.close();
    }
    vm.$http.post('/service/coop/join?id=' + id).then(function(response) {
        startCoopWebsocket(id)

        vm.blikjes = response.data;
        vm.coopId = id;
        vm.coopStarted = true;
        document.querySelector('dialog').close();
    });
}

function startCoopWebsocket(id) {
    var wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    var wsUrl = wsProtocol + '//' + window.location.host + '/ws/coop/' + id;
    // var wsUrl = 'wss://blikje.jorith.nl/ws/coop/' + id;

    coopSocket = new ReconnectingWebSocket(wsUrl);

    coopSocket.onmessage = function(evt) {
        var data = JSON.parse(evt.data)

        console.log(vm.blikjes);
        var blikje = vm.blikjes.find(function(x) {
            return x.id === data.blikje;
        });

        if (data.add) {
            blikje.amount += 1;
            updateTotal();
        } else if (data.substract) {
            blikje.amount -= 1;
            updateTotal();
        }
    };

    coopSocket.onerror = function(evt) {
        console.log('Error in Websocket communication',evt);
    };
}

function coopPlusOne(blikje) {
    var data = {
        blikje: blikje.id,
        add: true
    }
    coopSocket.send(JSON.stringify(data));
}

function coopMinusOne(blikje) {
    var data = {
        blikje: blikje.id,
        substract: true
    }
    coopSocket.send(JSON.stringify(data));
}


window.onbeforeunload = function (e) {
    if (vm.stateChanged) {
        return "Ingevulde gegevens gaan verloren.";
    } else {
        return undefined;
    }
};

if ('serviceWorker' in navigator) {
  window.addEventListener('load', function() {
    navigator.serviceWorker.register('/sw.js').then(function(registration) {
      // Registration was successful
      console.log('ServiceWorker registration successful with scope: ', registration.scope);
    }, function(err) {
      // registration failed :(
      console.log('ServiceWorker registration failed: ', err);
    });
  });
}
var app = angular.module('blikje', []);

app.controller('blikjeCtrl', ['$scope', '$http', function($scope, $http) {
	$scope.stateChanged = false;
	$scope.totalAmount = 0;
	
	$http.get('/global/json/blikjes.json').success(function(data) {
		$scope.blikjes = data;
	});

	window.onbeforeunload = function (e) {
		if ($scope.stateChanged && !$scope.orderSent) {
			return "Ingevulde gegevens gaan verloren.";
		} else {
			return undefined;
		}
	};

	$scope.plusOne = function(blikje) {
		blikje.amount += 1;
		updateTotal();
	};

	$scope.minusOne = function(blikje) {
		if (blikje.amount > 0) {
			blikje.amount -= 1;
			updateTotal();
		}
	};
	
	$scope.isOrderTime = function() {
		var now = new Date();
		
		var orderDay 		= 5;
		var orderStartHour 	= 12;
		var orderEndHour 	= 18;

		return now.getDay() == orderDay
			&& now.getHours() >= orderStartHour
			&& now.getHours() <= orderEndHour;
	}
	
	$scope.sendOrder = function() {
		var confirmed = confirm('Bestelling verzenden?');

		if (confirmed) {
			var order = {};

			angular.forEach($scope.blikjes, function(blikje) {
				if (blikje.amount > 0) {
					order[blikje.id] = blikje.amount;
				}
			});

			$http.post('/api/blikjesteller/send-order', order)
				.then(function(response) {
					alert('Verzonden!');
					$scope.orderSent = true;
				});
		}
	};

	function updateTotal() {
		$scope.stateChanged = true;
		var totalAmount = 0;

		angular.forEach($scope.blikjes, function(blikje) {
		  totalAmount += (blikje.price * blikje.amount);
		});

		$scope.totalAmount = totalAmount;
		$scope.orderSent = false;
	}
}]);
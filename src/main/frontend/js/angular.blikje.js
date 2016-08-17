var app = angular.module('blikje', []);

app.controller('blikjeCtrl', ['$scope', '$http', function($scope, $http) {
	$scope.stateChanged = false;
	$scope.totalAmount = 0;

	$scope.debugMode = getParameterByName('debug') === 'true';
	$http.defaults.headers.common['X-Debug'] = $scope.debugMode;

	$http.get('/rest/blikjesteller/blikjes').success(function(data) {
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

			$http.post('/rest/blikjesteller/send-order', order)
				.then(function(response) {
					alert('Verzonden!');
					$scope.orderSent = true;
					
				}, function errorCallback(response) {
				   console.log(response);
				   alert('Bestelling kon niet worden verzonden.');
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
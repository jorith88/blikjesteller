var app = angular.module('blikje', []);

app.controller('blikjeCtrl', ['$scope', '$http', function($scope, $http) {
	$scope.stateChanged = false;
	$scope.totalAmount = 0;

	$http.get('/api/blikjesteller/blikjes').success(function(data) {
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

	$scope.sendOrder = function() {
		var confirm = confirm('Bestelling verzenden?');
		
		if (confirm) {
			var order = {};
			
			angular.forEach($scope.blikjes, function(blikje) {
				if (blikje.amount > 0) {
					order[blikje.id] = blikje.amount;
				}
			});
			
			$http.post('/api/blikjesteller/send-order', order).then(function() {
				$scope.orderSent = true;
				console.log('order sent 1');
				alert('Bestelling verzonden!');
				console.log('order sent 2');
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
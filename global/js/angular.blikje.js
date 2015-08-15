var app = angular.module('blikje', []);

app.controller('blikjeCtrl', function($scope) {
	$scope.stateChanged = false;
	$scope.totalAmount = 0;
	$scope.blikjes = [
		{'name': 'Cola', 'price': 0.7, 'amount': 0},
		{'name': 'Cola Zero', 'price': 0.7, 'amount': 0},
		{'name': 'Fanta', 'price': 0.7, 'amount': 0},
		{'name': 'Minute Maid', 'price': 0.7, 'amount': 0},
		{'name': 'Ice tea', 'price': 0.7, 'amount': 0},
		{'name': '7-Up', 'price': 0.7, 'amount': 0},
	];
	
	window.onbeforeunload = function (e) {
		if ($scope.stateChanged) {
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
	
	function updateTotal() {
		$scope.stateChanged = true;
		var totalAmount = 0;
		angular.forEach($scope.blikjes, function(blikje) {
		  totalAmount += (blikje.price * blikje.amount);
		});
		$scope.totalAmount = totalAmount;
	}
});
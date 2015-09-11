var app = angular.module('blikje', []);

app.controller('blikjeCtrl', function($scope) {
	$scope.stateChanged = false;
	$scope.totalAmount = 0;
	$scope.blikjes = [
		{'name': 'Coca Cola', 'price': 0.7, 'amount': 0},
		{'name': 'Coca Cola Zero', 'price': 0.7, 'amount': 0},
		{'name': 'Spa Rood', 'price': 0.7, 'amount': 0},
		{'name': 'Fanta Orange', 'price': 0.7, 'amount': 0},
		{'name': 'Minute Maid', 'price': 1, 'amount': 0},
		{'name': 'Hero Cassis', 'price': 0.8, 'amount': 0},
		{'name': 'Lipton Ice Tea', 'price': 0.8, 'amount': 0},
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

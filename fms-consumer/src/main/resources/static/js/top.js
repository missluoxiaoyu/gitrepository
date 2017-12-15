app.controller('promptInfo', function($scope) {
		 $scope.myVar = true;
        $scope.click = function() {
       	 $scope.myVar = !$scope.myVar;
        };
	});
 angular.module('myApp', ['tm.pagination']).controller('testController', function($scope, $timeout){
   
        $scope.paginationConf = {
            currentPage: 1,
            totalItems: 8000,
            itemsPerPage: 15,
            pagesLength: 15,
            perPageOptions: [10, 20, 30, 40, 50],
            onChange: function(){
            }
        };


    })
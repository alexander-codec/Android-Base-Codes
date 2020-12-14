// Initialize your app
var myApp = new Framework7({
    animateNavBackIcon: true,
    // Enable templates auto precompilation
    precompileTemplates: true,
    // Enabled pages rendering using Template7
	swipeBackPage: false,
	swipePanelOnlyClose: true,
	pushState: true,
    template7Pages: true
});

// Export selectors engine
var $$ = Dom7;

// Add main View
var mainView = myApp.addView('.view-main', {
    // Enable dynamic Navbar
    dynamicNavbar: false,
});


$(document).ready(function() {
		$("#RegisterForm").validate();
		$("#LoginForm").validate();
		$("#ForgotForm").validate();
		$('.close-popup').on('click', function() {
			 $("label.error").hide();
		});
});

$$(document).on('pageInit', function (e) {
		$("#RegisterForm").validate();
		$("#LoginForm").validate();
		$("#ForgotForm").validate();
		$('.close-popup').on('click', function() {
			 $("label.error").hide();
		});	
		(function() {
			[].slice.call( document.querySelectorAll( 'select#selectoptions' ) ).forEach( function(el) {	
				new SelectFx(el, {
					stickyPlaceholder: false
				});
			} );
		})();
})

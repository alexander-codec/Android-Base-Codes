function mesalvar() {
	  var user = firebase.auth().currentUser;
      var nomecompleto = document.getElementById('nomecompleto').value;
		  if (nomecompleto.length < 4) {
			  alert('Insira seu nome completo.');
			  return;
			}
		  user.updateProfile({
			  displayName: nomecompleto
			}).then(function() {
			  window.location.href = "home.html";
			}, function(error) {
			  alert('Ocorreu um erro');
			  return;
			});
}

function initApp() {
      // Listening for auth state changes.
      // [START authstatelistener]
      firebase.auth().onAuthStateChanged(function(user) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        if (user) {
          // User is signed in.
		  console.log(JSON.stringify(user, null, '  '));
          // [END_EXCLUDE]
        } else {
          // User is signed out.
          // [START_EXCLUDE silent]
		  window.location.href = "acessar.html";
          // [END_EXCLUDE]
        }
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
      });
      // [END authstatelistener]
      document.getElementById('salvar').addEventListener('click', mesalvar, false);
    }
    window.onload = function() {
      initApp();
    };
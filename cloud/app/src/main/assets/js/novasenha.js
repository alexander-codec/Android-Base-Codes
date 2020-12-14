function menovasenha() {
      var email = document.getElementById('email').value;
      // [START sendpasswordemail]
	  if (email.length < 4) {
        alert('Insira seu email.');
        return;
      }
      firebase.auth().sendPasswordResetEmail(email).then(function() {
        // Password Reset Email Sent!
        // [START_EXCLUDE]
        alert('Nova senha enviada!');
        // [END_EXCLUDE]
      }).catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        // [START_EXCLUDE]
        if (errorCode == 'auth/invalid-email') {
          alert('Email Invalido');
        } else if (errorCode == 'auth/user-not-found') {
          alert('Usuario não existe');
        }
        console.log(error);
        // [END_EXCLUDE]
      });
      // [END sendpasswordemail];
    }

function initApp() {
      // Listening for auth state changes.
      // [START authstatelistener]
      firebase.auth().onAuthStateChanged(function(user) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        if (user) {
          // User is signed in.
		  window.location.href = "home.html";
          // [END_EXCLUDE]
        } else {
          // User is signed out.
          // [START_EXCLUDE silent]
		  console.log("Usuario Deslogado");
          // [END_EXCLUDE]
        }
        // [START_EXCLUDE silent]
        document.getElementById('novasenha').disabled = false;
        // [END_EXCLUDE]
      });
      // [END authstatelistener]
      document.getElementById('novasenha').addEventListener('click', menovasenha, false);
    }
    window.onload = function() {
      initApp();
    };
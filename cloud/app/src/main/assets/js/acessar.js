function meacessar() {
      if (firebase.auth().currentUser) {
        // [START signout]
        firebase.auth().signOut();
        // [END signout]
      } else {
        var email = document.getElementById('email').value;
        var password = document.getElementById('password').value;
        if (email.length < 4) {
          alert('Insira seu email.');
          return;
        }
        if (password.length < 4) {
          alert('Insira sua senha.');
          return;
        }
        // Sign in with email and pass.
        // [START authwithemail]
        firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
          // Handle Errors here.
          var errorCode = error.code;
          var errorMessage = error.message;
          // [START_EXCLUDE]
          if (errorCode === 'auth/wrong-password') {
            alert('Senha incorreta.');
          } else {
            alert(errorMessage);
          }
          console.log(error);
          document.getElementById('acessar').disabled = false;
          // [END_EXCLUDE]
        });
        // [END authwithemail]
      }
      document.getElementById('acessar').disabled = true;
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
        document.getElementById('acessar').disabled = false;
        // [END_EXCLUDE]
      });
      // [END authstatelistener]
      document.getElementById('acessar').addEventListener('click', meacessar, false);
    }
    window.onload = function() {
      initApp();
    };
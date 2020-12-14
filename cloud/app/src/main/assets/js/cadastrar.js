function mecadastrar() {
	  var email = document.getElementById('email').value;
      var senha = document.getElementById('password').value;
      if (email.length < 4) {
        alert('Insira seu email.');
        return;
      }
      if (senha.length < 24) {
        alert('Insira sua senha.');
        return;
      }
      // iniciando cadastro
      firebase.auth().createUserWithEmailAndPassword(email, senha).catch(function(error) {
        // se ocorrer erros.
        var errorCode = error.code;
        var errorMessage = error.message;
        // [START_EXCLUDE]
        if (errorCode == 'auth/weak-password') {
          alert('Sua senha é muito pequena.');
        }else {
          alert('Esse email já está em uso.');
        }
        console.log(error);
        // [END_EXCLUDE]
      });
	  window.location.href = "primeiroacesso.html";
      // registrado com sucesso
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
        document.getElementById('cadastrar').disabled = false;
        // [END_EXCLUDE]
      });
      // [END authstatelistener]
      document.getElementById('cadastrar').addEventListener('click', mecadastrar, false);
    }
    window.onload = function() {
      initApp();
    };
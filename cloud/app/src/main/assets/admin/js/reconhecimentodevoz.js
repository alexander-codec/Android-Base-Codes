//reconhecer clique no microfone
$('#microfone').click(function() {
  var recognition = new webkitSpeechRecognition();
  console.log("reconhecendo voz");
  animacaovoz();
  timeout();
  recognition.onresult = function(event) {
    microfoneacao(event.results[0][0].transcript);
  }
  recognition.start();
});
//Executar ação apos clique no microfone
function microfoneacao(text) {
	console.log("voz reconhecida");
	animacaovozfinal();
  /* $('#microfonetexto').append('<div role="alert" class="alert alert-success alert-icon alert-icon-colored alert-dismissible"><div class="icon"><span class="mdi mdi-check"></span></div><div class="message"><button type="button" data-dismiss="alert" aria-label="Close" class="close"><span aria-hidden="true" class="mdi mdi-close"></span></button><strong>Teste para sistema de pequisa!</strong>   ' + text + '</div></div>'); */
  botSpeak(text);
}
//resposta por voz
function botSpeak(text) {
  if ('speechSynthesis' in window) {
    var msg = new SpeechSynthesisUtterance(text);
    window.speechSynthesis.speak(msg);
  }
}
//executar ação para resposta em voz exemplo -- respostaporvoz('Oi, sou sua assistente pessoal');
function respostaporvoz(text) {
  botSpeak(text);
}
function animacaovoz() {
  $('#microfonetexto').html('<div id="container">Fale Agora <span class="blue"></span><span class="red"></span><span class="yellow"></span><span class="green"></span></div>');
}
function animacaovozfinal() {
  $('#microfonetexto').html('');
}
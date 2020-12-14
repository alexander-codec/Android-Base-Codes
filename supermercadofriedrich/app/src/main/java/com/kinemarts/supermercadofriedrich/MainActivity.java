package com.kinemarts.supermercadofriedrich;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result)
            {
                Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
                result.confirm();
                return true;
            }
        });
        // causa o arquivo que for carregar no webview for fora do app ativar essa opção para que quando não tenho conexão
        // deixar uma pagina de sem conexao
        /*webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Handle the error
                super.onReceivedError(view, errorCode, description, failingUrl);
                WebView webView = (WebView) findViewById(R.id.webView);
                webView.loadUrl("file:///android_asset/NoInternet.html");
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr){
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                WebView webView = (WebView) findViewById(R.id.webView);
                webView.loadUrl("file:///android_asset/NoInternet.html");
            }
        });*/
        webView.loadUrl("file:///android_asset/index.html");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"contato@kinemarts.com.br"});  //developer 's email
                Email.putExtra(Intent.EXTRA_SUBJECT,
                        "Feedback do aplicaivo My Messeger"); // Email 's Subject
                Email.putExtra(Intent.EXTRA_TEXT, "Olá," + "");  //Email 's Greeting text
                startActivity(Intent.createChooser(Email, "Enviar Feedback:"));
            }
        });*/
    }
}

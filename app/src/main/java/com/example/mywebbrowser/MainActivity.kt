package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //웹뷰 기본 설정
        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient() //이게 없으면 웹뷰에 페이지가 표시되지 않음, 자체동작

        }

        webView.loadUrl("http://www.google.com")

        //키보드 검색버튼 동작 정의
        urlEditText.setOnEditorActionListener { _, actionId, _ -> //텍스트에디터의 글자입력마다 호출
            if(actionId == EditorInfo.IME_ACTION_SEARCH){ //검색버튼이 눌러졌는지 확인
                webView.loadUrl(urlEditText.text.toString())
                true
                // true 값 반환으로 이벤트 종료
            } else{
                false
            }

        }

        //context 메뉴 등록(웹뷰를 길게 누르면 나옴)
        registerForContextMenu(webView)

    }

    //뒤로가기 기능 실행 (이게 없으면 뒤로가기 할때 종료됨)
    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }
        else {
            super.onBackPressed()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(
                R.menu.main,
                menu
            ) //메뉴 위치 지정
//            return true
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("http://www.google.com")
                return true
            }
            R.id.action_naver -> {
                webView.loadUrl("http://www.naver.com")
            }
            R.id.action_daum -> {
                webView.loadUrl("http://www.daum.net")
            }
            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-123-4567")
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                //문자 보내기 anko 사용
                sendSMS("031-123-4567", webView.url)
                return true
            }
            R.id.action_email -> {
                //이메일 보내기 anko 사용
                email("test@example.com", "좋은 사이트", webView.url)
                
                return true
            }


        }

        return super.onOptionsItemSelected(item)
    }

    //context 메뉴 등록
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    //context 메뉴 클릭 이벤트 처리
    override fun onContextItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){
            R.id.action_share->{
                //페이지 공유 anko 사용
                share(webView.url)
                return true
            }
            R.id.action_browser->{
                // 기본 웹 브라우저에서 열기 anko 사용
                 browse(webView.url)
                return true
            }
        }


        return super.onContextItemSelected(item)
    }

}

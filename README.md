# WebView Bridge Demo (Android + Web)

Este projeto e uma demo focada em um cenario bem comum em apps modernos: **telas web rodando dentro de WebView que precisam acionar fluxos nativos** (e receber a resposta de volta).

Aqui voce encontra um exemplo completo de **comunicacao Web <-> Android** usando `addJavascriptInterface` + `evaluateJavascript`, com **UI bonita** (web e Compose) e **MVVM organizado**.

---

## 🚀 O que essa demo faz

Fluxo principal:

1. A pagina web dispara uma requisicao: `AndroidAuth.request(payload)`
2. O Android recebe a mensagem e abre um dialog nativo (Compose) para o usuario **Autorizar** ou **Cancelar**
3. O Android devolve a resposta para a web chamando `window.onAuthResult(...)`
4. A pagina atualiza o status e registra os eventos em um painel de atividade (inclui timeout e modo preview)

---

## 🎯 Por que isso importa

Muitos apps usam WebView para acelerar entrega e reaproveitar telas web, mas ainda precisam de recursos nativos (ex.: login/SSO, permissoes, biometria, aprovacao, pagamentos).

Essa demo mostra um padrao simples e escalavel para:

- manter um **contrato de mensagens** claro entre web e Android
- correlacionar respostas com `requestId`
- evitar acoplamento entre ViewModel e `WebView`
- separar **estado**, **intencoes** e **efeitos** (side effects)

---

## 🧱 Arquitetura (MVVM)

- **Domain** (model puro): `app/src/main/java/com/example/webserviceexemple/domain/auth/AuthModels.kt`
- **Contract** (UiState/Intent/Effect): `app/src/main/java/com/example/webserviceexemple/presentation/auth/WebAuthContract.kt`
- **ViewModel** (estado + efeitos): `app/src/main/java/com/example/webserviceexemple/presentation/auth/WebAuthViewModel.kt`
- **UI (Compose + WebView)**: `app/src/main/java/com/example/webserviceexemple/presentation/auth/WebAuthScreen.kt`
- **Bridge** (Android interface para web): `app/src/main/java/com/example/webserviceexemple/presentation/webview/AndroidAuthBridge.kt`
- **Web** (pagina da demo): `app/src/main/assets/web/index.html` e `app/src/main/assets/web/app.js`

---

## 🛠️ Tecnologias

- Kotlin
- Jetpack Compose (Material 3)
- Android WebView (`addJavascriptInterface`, `evaluateJavascript`)
- HTML/CSS/JS (pagina em `assets/`)

---

## ▶️ Como executar

1. Abra o projeto no Android Studio
2. Sincronize o Gradle
3. Rode o app em um emulador ou dispositivo
4. Na pagina web, clique em **"Solicitar autenticacao"**
5. No dialog nativo, selecione **Autorizar** ou **Cancelar**

---

## 📝 Observacoes

- A pagina web tem **modo preview** quando a bridge Android nao esta disponivel (simula uma resposta).
- O callback `window.onAuthResult(...)` aceita string JSON (padrao da integracao) e trata payloads invalidos de forma defensiva.

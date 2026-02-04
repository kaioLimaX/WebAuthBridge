(() => {
  const ui = {
    bridgeStatus: document.getElementById("bridgeStatus"),
    bridgeDot: document.getElementById("bridgeDot"),
    bridgeText: document.getElementById("bridgeText"),
    btnRequest: document.getElementById("btnRequest"),
    btnReset: document.getElementById("btnReset"),
    lastRequestId: document.getElementById("lastRequestId"),
    lastOrigin: document.getElementById("lastOrigin"),
    lastStatus: document.getElementById("lastStatus"),
    payloadBox: document.getElementById("payloadBox"),
    logList: document.getElementById("logList")
  };

  const pending = new Map();

  function nowTime() {
    return new Date().toLocaleTimeString(undefined, {
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit"
    });
  }

  function safeJsonParse(value) {
    if (value == null) return null;
    if (typeof value === "object") return value;
    if (typeof value !== "string") return null;
    try {
      return JSON.parse(value);
    } catch (_e) {
      return null;
    }
  }

  function isAndroidBridgeAvailable() {
    return typeof window.AndroidAuth !== "undefined" &&
      window.AndroidAuth &&
      typeof window.AndroidAuth.request === "function";
  }

  function setBridgeStatus() {
    const ok = isAndroidBridgeAvailable();

    ui.bridgeDot.className = ok ? "dot ok" : "dot";
    ui.bridgeText.textContent = ok
      ? "Bridge ativa (Android)"
      : "Modo preview (sem Android)";
  }

  function renderPayload(payload) {
    ui.lastRequestId.textContent = payload.requestId;
    ui.lastOrigin.textContent = isAndroidBridgeAvailable() ? "android-webview" : "browser";
    ui.lastStatus.textContent = "WAITING";
    ui.payloadBox.textContent = JSON.stringify(payload, null, 2);
  }

  function setStatus(status) {
    ui.lastStatus.textContent = status;
  }

  function addLog(kind, message) {
    const li = document.createElement("li");
    li.className = "logItem";

    const head = document.createElement("div");
    head.className = "logHead";

    const tag = document.createElement("span");
    tag.className = "tag";
    tag.textContent = kind;

    if (kind === "SUCCESS") tag.className = "tag ok";
    else if (kind === "CANCELLED" || kind === "ERROR") tag.className = "tag bad";
    else if (kind === "WAIT") tag.className = "tag wait";

    const time = document.createElement("span");
    time.className = "time";
    time.textContent = nowTime();

    const msg = document.createElement("div");
    msg.className = "msg";
    msg.textContent = message;

    head.appendChild(tag);
    head.appendChild(time);
    li.appendChild(head);
    li.appendChild(msg);

    ui.logList.prepend(li);
  }

  function sendAuthRequest(payload) {
    renderPayload(payload);
    addLog("WAIT", `Request enviada (requestId=${payload.requestId})`);
    ui.btnRequest.disabled = true;

    return new Promise((resolve, reject) => {
      const timeoutId = setTimeout(() => {
        if (!pending.has(payload.requestId)) return;
        pending.delete(payload.requestId);
        setStatus("ERROR");
        addLog("ERROR", `Timeout aguardando resposta (requestId=${payload.requestId})`);
        ui.btnRequest.disabled = false;
        reject(new Error("Timeout"));
      }, 12000);

      pending.set(payload.requestId, { resolve, reject, timeoutId });

      const payloadString = JSON.stringify(payload);

      if (isAndroidBridgeAvailable()) {
        try {
          window.AndroidAuth.request(payloadString);
        } catch (e) {
          clearTimeout(timeoutId);
          pending.delete(payload.requestId);
          reject(e);
        }
        return;
      }

      // Browser preview: simula resposta do "Android".
      setTimeout(() => {
        const ok = Math.random() > 0.35;
        window.onAuthResult({
          requestId: payload.requestId,
          status: ok ? "SUCCESS" : "CANCELLED"
        });
      }, 700);
    });
  }

  function requestAuth() {
    const payload = {
      requestId: Date.now().toString(),
      user: "Caio",
      action: "LOGIN",
      message: "Deseja autenticar este acesso?"
    };

    return sendAuthRequest(payload)
      .catch((e) => {
        addLog("ERROR", `Falha ao enviar request: ${String(e && e.message ? e.message : e)}`);
        setStatus("ERROR");
      })
      .finally(() => {
        ui.btnRequest.disabled = false;
      });
  }

  function clearState() {
    for (const entry of pending.values()) {
      if (entry && entry.timeoutId) clearTimeout(entry.timeoutId);
    }
    pending.clear();
    ui.btnRequest.disabled = false;
    ui.lastRequestId.textContent = "-";
    ui.lastOrigin.textContent = "-";
    ui.lastStatus.textContent = "-";
    ui.payloadBox.textContent = JSON.stringify(
      {
        requestId: "...",
        user: "Caio",
        action: "LOGIN",
        message: "Deseja autenticar este acesso?"
      },
      null,
      2
    );
    ui.logList.innerHTML = "";
    addLog("WAIT", "Estado limpo. Pronto para nova request.");
  }

  // Callback chamado pelo Android via evaluateJavascript.
  window.onAuthResult = function (responseJson) {
    const response = safeJsonParse(responseJson) || {};

    const requestId = response.requestId;
    const status = response.status;

    if (!requestId) {
      addLog("ERROR", "Resposta sem requestId (payload invalido).");
      setStatus("ERROR");
      return;
    }

    if (status === "SUCCESS") {
      setStatus("SUCCESS");
      addLog("SUCCESS", `Autenticacao aprovada (requestId=${requestId})`);
    } else if (status === "CANCELLED") {
      setStatus("CANCELLED");
      addLog("CANCELLED", `Autenticacao cancelada (requestId=${requestId})`);
    } else {
      setStatus("ERROR");
      addLog("ERROR", `Status desconhecido: ${String(status)} (requestId=${requestId})`);
    }

    const waiter = pending.get(requestId);
    if (waiter) {
      clearTimeout(waiter.timeoutId);
      pending.delete(requestId);
      waiter.resolve(response);
    }
  };

  ui.btnRequest.addEventListener("click", requestAuth);
  ui.btnReset.addEventListener("click", clearState);

  setBridgeStatus();
  addLog("WAIT", "Demo carregada. Clique em 'Solicitar autenticacao'.");

  // Atualiza o indicador quando a pagina volta ao foco.
  document.addEventListener("visibilitychange", () => {
    if (!document.hidden) setBridgeStatus();
  });
})();

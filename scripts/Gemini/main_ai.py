from gemini import GeminiAI
from flask import Flask, request

app = Flask(__name__)

@app.route('/gemini', methods=['POST'])
def process_message():
  message = request.form.get('message')
  if message:
    topicos, resumo = ia.send_message(message)
    return {
      "topicos": topicos,
      "resumo": resumo
    }
  else:
    return "Mensagem inválida", 400

@app.route('/shutdown', methods=['GET'])
def shutdown():
    func = request.environ.get('werkzeug.server.shutdown')
    if func is None:
        raise RuntimeError('Not running with the Werkzeug Server')
    func()
    return 'Server shutting down...'

if __name__ == "__main__":
  ia = GeminiAI()  # Inicialize a IA apenas uma vez
  app.run(debug=False) 
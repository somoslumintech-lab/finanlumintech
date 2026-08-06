# Finance

Aplicativo de finanças pessoais e compartilhadas, desenvolvido para automatizar o controle financeiro através da captura e interpretação de notificações bancárias.

## 🎯 Objetivo

O Finance permite que uma pessoa controle suas finanças sozinha ou compartilhe uma estrutura financeira com outras pessoas.

Possíveis cenários:

- 👤 Uso individual
- 💑 Casal
- 👥 Família
- 🤝 Sócios
- 🏢 Pequenos grupos

A principal proposta é reduzir ao máximo a necessidade de cadastrar manualmente despesas e receitas.

---

## 🚀 Conceito principal

O aplicativo será capaz de interpretar notificações financeiras recebidas no Android.

Exemplo:

> Compra aprovada R$ 87,90 — SUPERMERCADO XYZ

O Finance deverá identificar automaticamente:

- valor;
- estabelecimento;
- tipo da operação;
- banco/aplicativo de origem;
- cartão ou conta, quando disponível;
- data e horário;
- categoria;
- nível de confiança.

Depois disso, o evento poderá ser transformado automaticamente em uma transação financeira.

### Fluxo

```text
Notificação bancária
        ↓
Captura Android
        ↓
Normalização
        ↓
Parser financeiro
        ↓
Identificação da operação
        ↓
Confiança
        ↓
Deduplicação
        ↓
Evento financeiro
        ↓
Transação
        ↓
Dashboard

📱 Plataforma

Primeira plataforma:

Android
Kotlin
Jetpack Compose

O projeto está sendo desenvolvido inicialmente pelo celular.

🧠 Captura automática

O aplicativo utilizará o mecanismo de notificações do Android para receber eventos disponibilizados ao serviço de notificações autorizado pelo usuário.

A captura será separada do processamento financeiro.

NotificationListener
        ↓
NotificationNormalizer
        ↓
FinancialNotificationParser
        ↓
FinancialEvent

🏦 Backend

Backend:

Supabase
PostgreSQL
Row Level Security (RLS)

O banco foi estruturado para suportar:

usuários;
workspaces;
membros;
contas;
cartões;
categorias;
transações;
transferências;
settlements;
budgets;
goals;
transações recorrentes;
eventos financeiros;
origem das transações.

🗄️ Arquitetura financeira

O Finance trabalha com o conceito de workspace.

Um workspace pode representar:

Pessoa
Casal
Família
Sócios
Empresa/pequeno grupo
Os membros possuem acesso controlado aos dados daquele workspace.

🔔 Notification Parser

O parser será responsável por transformar texto de notificações em dados financeiros estruturados.

Exemplo:

Compra aprovada
R$ 87,90
SUPERMERCADO XYZ
Cartão final 1234

Resultado esperado:
{
  "amount": 87.90,
  "merchant": "SUPERMERCADO XYZ",
  "type": "expense",
  "card_last_four": "1234",
  "confidence": 0.90
}

🎯 Confiança

O sistema não deverá registrar cegamente qualquer notificação como transação.

A interpretação será acompanhada por um nível de confiança.

Proposta inicial:

Confiança
Comportamento
≥ 90%
Criar automaticamente
60–89%
Solicitar confirmação
< 60%
Não criar automaticamente

Esses valores poderão ser ajustados durante os testes reais.

🛡️ Deduplicação

Uma mesma operação pode gerar mais de uma notificação.
Por isso o sistema terá mecanismo de deduplicação antes de criar uma transação.

O objetivo é impedir:

1 compra
↓
2 notificações
↓
2 despesas
O resultado esperado:
1 compra
↓
2 notificações
↓
1 transação

🔐 Segurança

Informações sensíveis não devem ser armazenadas no código-fonte.

Nunca enviar para o GitHub:

service_role_key
senhas
tokens privados
chaves privadas
keystores
.env
local.properties
O projeto utiliza .gitignore para impedir o versionamento acidental de arquivos sensíveis.

🏗️ Roadmap

V0.1 — Estrutura inicial
[x] Criar projeto Android
[x] Configurar Gradle
[x] Criar repositório GitHub
[x] Criar .gitignore
[x] Criar documentação inicial

V0.2 — Aplicativo mínimo
[ ] AndroidManifest
[ ] MainActivity
[ ] Jetpack Compose
[ ] Primeira tela
[ ] Gerar primeiro APK
[ ] Instalar no celular

V0.3 — Captura de notificações
[ ] NotificationListenerService
[ ] Solicitação de permissão
[ ] Captura local
[ ] Logs
[ ] Testes com notificações reais

V0.4 — Parser financeiro
[ ] Extração de valores
[ ] Identificação de estabelecimento
[ ] Detecção de compra
[ ] Detecção de Pix
[ ] Detecção de entrada
[ ] Detecção de transferência
[ ] Detecção de estorno
[ ] Confidence Score
[ ] Deduplicação

V0.5 — Supabase
[ ] Conectar aplicativo ao Supabase
[ ] Inserir financial_events
[ ] Processar eventos
[ ] Criar transactions_v2
[ ] Testar RLS

V0.6 — Dashboard
[ ] Saldo
[ ] Entradas
[ ] Despesas
[ ] Categorias
[ ] Extrato
[ ] Filtros
[ ] Detalhes da transação

V0.7 — Finanças compartilhadas
[ ] Convites
[ ] Membros
[ ] Permissões
[ ] Despesas compartilhadas
[ ] Divisão de despesas
[ ] Settlements

V0.8 — Inteligência financeira
[ ] Categorização automática
[ ] Reconhecimento de estabelecimentos
[ ] Regras personalizadas
[ ] Detecção de padrões
[ ] Sugestões financeiras

📌 Estado atual
Versão: 0.1.0
Status: Em desenvolvimento
Fase atual: Estrutura inicial do aplicativo Android.

⚠️ Observação

O Finance está em fase de desenvolvimento e os recursos descritos neste README representam o planejamento do produto. Funcionalidades podem sofrer alterações durante a implementação e os testes.
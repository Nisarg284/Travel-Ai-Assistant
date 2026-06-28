# 🧳 Travel Assistant AI

> AI-powered itinerary planner for Indian destinations — built with Spring Boot, LangChain4j, and RAG.

![Java](https://img.shields.io/badge/Java-17+-007396?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=springboot)
![LangChain4j](https://img.shields.io/badge/LangChain4j-RAG-00897B?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-Build-C62828?style=flat-square&logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-7B1FA2?style=flat-square)

---

## 📖 About

Travel Assistant AI generates personalised, day-by-day travel itineraries tailored to your **destination**, **trip duration**, and **budget** — no matter how tight.

It retrieves real destination knowledge via a RAG pipeline and produces a structured plan that actually fits your wallet.

> 💡 **₹5,000 for 1 day in Goa?** That's a plan — not a rejection.
> Hostel dorm ₹600 · Street food ₹400 · Scooter rental ₹300 · Beaches (free) = **~₹1,300 total**

---

## ✨ Features

- **Destination-aware planning** — RAG retrieves attraction, food, and transport data specific to the destination
- **LLM-powered feasibility gate** — validates trips before planning; only blocks genuinely impossible budgets (< ₹500/day)
- **Strict budget discipline** — adapts accommodation, food, and activities to fit any budget
- **Day-by-day itinerary** — structured Morning / Afternoon / Evening breakdown with meals and stay
- **Full cost breakdown** — accommodation, food, local travel, activities, miscellaneous
- **Practical travel tips** — destination-specific advice to save money and time
- **Pluggable LLM backend** — swap Claude, OpenAI, or any `ChatModel` via LangChain4j

---

## 🛠️ Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| Language | Java 25 | Core application language |
| Framework | Spring Boot | DI, REST, service layer |
| AI Orchestration | LangChain4j | LLM calls, RAG pipeline, agents |
| LLM | Claude / OpenAI | Feasibility check + itinerary planning |
| RAG | LangChain4j RAG | Destination knowledge retrieval |
| Build | Maven | Dependency management |

---

## 🏗️ Architecture

Each request flows through three sequential stages:

```
┌─────────────────────────────┐
│         User Request        │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│   TripFeasibilityValidator  │  ← LLM-powered gate (₹500/day floor)
└──────────────┬──────────────┘
               │  FEASIBLE
               ▼
┌─────────────────────────────┐
│        RAG Retriever        │  ← Pulls destination knowledge
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│      ItineraryPlanner       │  ← Generates day-wise itinerary
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│  Structured Itinerary       │
│  Response                   │
└─────────────────────────────┘
```

### Key Components

| Component | Role |
|---|---|
| `TripFeasibilityValidator` | LLM agent that checks budget viability. Blocks only truly impossible trips (below ₹500 × days). Never judges comfort or suggests upgrades. |
| `RAG Retriever` | Pulls destination-specific content — attractions, costs, transport, food — from the knowledge base. |
| `ItineraryPlanner` | Builds a structured prompt with retrieved context and generates the full itinerary via the chat model. |

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- An Anthropic or OpenAI API key

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/Nisarg284/Travel-Ai-Assistant.git
cd travel-assistant-ai

# 2. Add your API key to src/main/resources/application.properties
# langchain4j.anthropic.api-key=YOUR_KEY_HERE

# 3. Build and run
./mvnw spring-boot:run
```

---

## 📬 Usage

Send a POST request with your trip constraints:

```http
POST /api/plan
Content-Type: application/json

{
  "destination" : "Goa",
  "days"        : 1,
  "budget"      : 5000
}
```

### Sample Response Structure

```
## 🗺️ Trip Overview
Destination: Goa | Duration: 1 day | Budget: ₹5,000 | Estimated Spend: ₹1,350

## 💰 Budget Breakdown
| Category      | Estimated Cost (₹) |
|---------------|--------------------|
| Accommodation | ₹600               |
| Food          | ₹450               |
| Local Travel  | ₹200               |
| Activities    | ₹100               |
| Miscellaneous | ₹000               |
| Total         | ₹1,350             |

## 📅 Day 1 – Sun, Sand & Old Goa
Morning   – Baga Beach, Calangute walk
Afternoon – Basilica of Bom Jesus, Se Cathedral
Evening   – Anjuna flea market, sunset at Vagator
🍽️ Meals  – Breakfast: chai + poha ₹60 | Lunch: fish thali ₹150 | Dinner: beach shack ₹250
🏨 Stay   – Zostel Goa (dorm) ~₹600/night
💸 Day Spend – ₹1,350
```

---

## 💰 Budget Philosophy

Most travel apps assume mid-range spending. This assistant is calibrated to **real Indian budget travel**:

| Category | Budget Option (₹) | Mid Option (₹) |
|---|---|---|
| Accommodation / night | 300 – 800 (hostel / dorm) | 800 – 2,000 (guesthouse) |
| Breakfast | 50 – 100 (chai + idli) | 150 – 250 |
| Lunch | 80 – 150 (thali / dhaba) | 200 – 400 |
| Dinner | 100 – 200 (local eatery) | 300 – 600 (restaurant) |
| Local transport / day | 100 – 300 (bus / auto) | 300 – 600 (taxi) |
| Activities / day | 0 – 200 (beaches / forts) | 300 – 800 (water sports) |

> ⚠️ **Feasibility rule:** trips are only blocked when the budget is below **₹500 × number of days** — the absolute floor for dorm + street food + bus. Everything above this gets a full itinerary.

---

## 📁 Project Structure

```
src/main/java/com/n23/foa/travelassistant/
├── agents/
│   ├── ItineraryPlanner.java               # Planner agent interface
│   └── TripFeasibilityExpert.java          # Validator agent interface
├── dto/
│   ├── TripConstraints.java                # Destination, days, budget
│   └── ValidationResult.java              # FEASIBLE / NOT FEASIBLE
├── service/
│   ├── DefaultItineraryPlanner.java        # Prompt builder + LLM call
│   ├── DefaultTripFeasibilityValidator.java
│   └── TripFeasibilityValidator.java       # Validator service interface
└── TravelAssistantApplication.java
```

---

## ⚙️ Configuration

```properties
# src/main/resources/application.properties

# LLM provider (Anthropic)
langchain4j.anthropic.api-key=YOUR_ANTHROPIC_API_KEY
langchain4j.anthropic.model-name=claude-sonnet-4-6

# LLM provider (OpenAI — alternative)
# langchain4j.open-ai.api-key=YOUR_OPENAI_API_KEY
# langchain4j.open-ai.model-name=gpt-4o

# RAG
langchain4j.rag.embedding-store.in-memory=true
```

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

<p align="center">Built with ❤️ using <b>Spring Boot</b> + <b>LangChain4j</b> + <b>RAG</b></p>

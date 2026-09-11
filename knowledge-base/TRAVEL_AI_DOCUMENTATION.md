# 🧳 Travel AI Assistant — Complete Project Specs & Architecture Documentation

Welcome to the comprehensive technical documentation for the **Travel AI Assistant** application. This project is a state-of-the-art, agentic AI-powered travel planner specifically tailored for Indian destinations. It leverages a modern web frontend in **Next.js** and an advanced multi-agent orchestrator backend in **Java 25 (Spring Boot & LangChain4j)**.

This document provides a highly detailed analysis of the backend architecture, frontend interface design, the multi-agent system, the customized RAG pipeline, the exact technologies and libraries used, and their step-by-step feature workflows.

---

## 🛠️ Complete Technology Stack & Dependencies

The Travel AI application is built on a decoupled, asynchronous client-server architecture:

```
┌─────────────────────────────────┐                 ┌─────────────────────────────────┐
│     FRONTEND (Next.js v16)      │  HTTP (REST)    │    BACKEND (Spring Boot 3.x)    │
│  - React 19 / TypeScript        ├────────────────>│  - Java 25 / LangChain4j        │
│  - Tailwind CSS v4              │                 │  - Qdrant Cloud Vector Store    │
│  - Framer Motion (Animations)   │                 │  - Hugging Face / OpenRouter LLM│
└─────────────────────────────────┘                 └─────────────────────────────────┘
```

### 1. Backend Service Layer (Spring Boot)
*   **Core Platform & Execution**:
    *   **Java 25 (JDK 25)**: Harnesses modern core language features (virtual threads, records, pattern matching, sequencers).
    *   **Spring Boot 3.x Starter Parent (Version 4.1.0-SNAPSHOT/Development version)**: Provides DI (Dependency Injection), auto-configuration, REST services, and asynchronous WebClient filters.
*   **AI Integration & LLM Orchestration**:
    *   **LangChain4j (v1.15.1)**: Deployed to coordinate chat connections, RAG pipelines, chat memory tracking, agent tool injections, extraction schemes, and AI Services.
    *   **OpenRouter Chat Integration (`langchain4j-open-ai-spring-boot4-starter`)**: Connected using the OpenAI-compatible wrapper to OpenRouter models (specifically Llama-3.3-70b-instruct or custom Sonnet engines).
    *   **Hugging Face Embeddings (`langchain4j-hugging-face`)**: Queries the Hugging Face Inference API (`all-MiniLM-L6-v2`) to generate 384-dimensional dense semantic vectors.
*   **Data Storage & Search**:
    *   **Qdrant Vector Database (`langchain4j-qdrant`)**: Holds high-dimensional text embeddings in a structured, metadata-indexed secure store.
*   **File Parsing & Utilities**:
    *   **Lombok**: Reduces boilerplate using annotation processors (`@Slf4j`, `@Getter`, `@Setter`, etc.).
    *   **Apache Tika Document Parser (`langchain4j-document-parser-apache-tika`)**: Extracts raw text blocks from unstructured user data (PDF, DOCX, TXT) to upload travel knowledge.

---

### 2. Frontend Client (Next.js Application)
*   **Core Framework**:
    *   **Next.js (v16.2.10)** with **React 19.2.4**: Implements rapid client-side rendering with App Router mechanics.
    *   **TypeScript (v5.x)**: Type safety enforces clean interface contracts between REST API payloads and UI render components.
*   **Styling & Design System**:
    *   **Tailwind CSS (v4.0.0)** & **@tailwindcss/postcss**: Uses utility class tokens combined with custom CSS imports. Look and feel tailored to feel active, dynamic, and premium.
    *   **Lucide React (v1.23.0)**: Clean SVG inline vector icons for layout and stat pills.
*   **Content & Animations Rendering**:
    *   **Framer Motion (v12.4.2)**: Orchestrates layout elevations, smooth transitions, drag handles, and interactive UI micro-animations.
    *   **React Markdown (v10.1.0) & Remark GFM (v4.0.1)**: Formats streaming answers, lists, tables, and bold headers in the chat dashboard.

---

## 🏗️ Backend System Architecture & Orchestration

The backend architecture uses a modular, multi-layered design. Instead of a single pipeline, the system utilizes a **Multi-Agent Supervisor** flow that divides work among specialized java tools.

![Application Architecture and Execution Flow](C:\Users\Nisarg\.gemini\antigravity\brain\88679f8c-3a57-4051-afb6-9e597f9846f3\exact_system_flow_chart_1783506256517.png)

```mermaid
graph TD
    classDef default fill:#111e30,stroke:#1c2c42,stroke-width:2px,color:#f4efe6;
    classDef greenTool fill:#43a047,stroke:#1c2c42,stroke-width:2px,color:#ffffff;
    classDef blueTool fill:#1e88e5,stroke:#1c2c42,stroke-width:2px,color:#ffffff;
    classDef subNode fill:#1c2c42,stroke:#1c2c42,stroke-width:1px,color:#8b9aae;

    User[User] --> TravelRequestClassifier[TravelRequestClassifier]

    TravelRequestClassifier -->|QUESTION| TravelKnowledgeAgent[TravelKnowledgeAgent RAG Q&A]
    TravelRequestClassifier -->|ITINERARY| SupervisorAgent["🧠 Supervisor Agent"]

    SupervisorAgent -->|@Tool| ResearchAgent[ResearchAgent RAG Knowledge]
    SupervisorAgent -->|@Tool| BudgetAgent[BudgetAgent Feasibility]
    SupervisorAgent -->|@Tool| HotelSearch[HotelSearchTool Mock Hotels]:::greenTool
    SupervisorAgent -->|@Tool| TransportSearch[TransportSearchTool Mock Routes]:::blueTool
    SupervisorAgent -->|@Tool| LocalExpert[LocalExpertAgent Food & Tips]

    ResearchAgent --> QdrantKB[Qdrant KB]:::subNode
    HotelSearch --> MockHotel[MockHotelService 38 hotels, 4 cities]:::subNode
    TransportSearch --> MockTransport[MockTransportService 25+ routes]:::subNode
```

---

### Step-by-Step Backend Flow:

### 1. Smart Intent Router (`TravelRequestClassifier`)
Every user prompt to `/ai/ask/{sessionId}` is intercepted by `DefaultTravelAssistant` and classified using `TravelRequestClassifier`.
*   Rather than naive keyword matching, this uses a declarative **LangChain4j AI Service** `@SystemMessage` to classify the input as:
    *   `QUESTION`: Miscellaneous Q&A, greetings, general facts ("What is the best month to visit Kerala?"). It is routed to `TravelKnowledgeAgent` using direct semantic search and conversational history.
    *   `ITINERARY`: Plans, edits, or budget modifications ("Plan a 3-day trip to Manali under 15k", "make it cheaper"). It is routed to the advanced multi-agent orchestrator.

### 2. Constraints Extraction & Conversation Context Memory
The `DefaultTravelPlanner` processes itinerary requests.
*   **TripConstraintsExtractor**: Automatically parses structured metadata parameters (`TripConstraints`) directly from natural text:
    *   `destination` (String)
    *   `days` (Integer)
    *   `budget` (String)
*   **Session Context-Aware Cache**: A thread-safe `ConcurrentHashMap` stores `TripConstraints` against the `sessionId`. For a prompt containing partial updates (e.g. "make it cheaper" or "use hostels only"), the system merges the inputs (retaining destination and duration while adjusting budget parameters).

### 3. LLM Feasibility Gate (`TripFeasibilityValidator`)
Before committing to the heavy planning loops, the system checks viability using RAG:
*   It checks the user's constraints against destination cost data fetched from the vector store.
*   **Budget Floor Check**: The system checks if the budget meets the required resource limit (generally **₹500 per day** for single occupants under hostel dorm + local bus rules).
*   If the budget is too low, the system stops execution and returns a detailed explanation and alternative suggestions, saving tokens and handling errors gracefully.

### 4. Supervisor Multi-Agent Collaboration Loop
If feasible, `TravelSupervisorAgent` (an orchestrator representing a declarative `AiService`) utilizes specialized, Java-based tools in a deterministic loop to build the final plan:
1.  **ResearchAgentTool**: Fetches core destination highlights, language, currency, and weather alerts from the knowledge base.
2.  **DestinationImageTool**: Fetches high-quality landscape photos for the destination using the `UnsplashImageService`.
3.  **ItineraryAgentTool**: Outlines curated day-by-day activities.
4.  **LocalExpertAgentTool**: Supplies cultural insights, safety tips, and local food suggestions.
5.  **HotelSearchTool**: Queries `MockHotelService` database to retrieve real accommodation options filtering by budget constraints and traveler style (backpacker, comfort, luxury).
6.  **TransportSearchTool**: Locates transit routes between departure and arrival hubs, detailing mode (Air, Train, Bus), operator, duration, and pricing.
7.  **BudgetAgentTool**: Synthesizes and allocates budget shares across Accommodation, Activities, Food, and Travel.

---

### Backend Reliability Features:
*   **API Key Round-Robin Proxy**: Overcomes external API token-per-minute (TPM) limits by using a JVM dynamic proxy. `ChatModelConfig` distributes API calls to multiple keys using index operations:
```java
private ChatModel createRoundRobin(ChatModel... models) {
    AtomicInteger index = new AtomicInteger(0);
    return (ChatModel) Proxy.newProxyInstance(
        ChatModel.class.getClassLoader(),
        new Class<?>[]{ChatModel.class},
        (proxy, method, args) -> {
            int i = Math.abs(index.getAndIncrement()) % models.length;
            return method.invoke(models[i], args);
        }
    );
}
```
*   **Automated Retries**: LLMs may occasionally produce malformed JSON. The backend handles this by retrying the `TravelSupervisorAgent` up to 2 times upon encountering an `OutputParsingException`.

---

## 🗃️ Advanced RAG & Ingestion System

RAG coordinates vector queries to build highly contextualized responses.

```
       [Markdown Documents (4 Destinations)]
                       │
                       ▼ 
         [MarkdownSectionExtractor]  <-- Custom Parser
                       │
                       ▼ 
             [TextSegment Chunks]    <-- Enriched with destination & section tags
                       │
                       ▼
          [Hugging Face MiniLM-L6]   <-- 384-dimension Vector Embeddings
                       │
                       ▼
           [Qdrant Vector Database]  <-- Cloud Instance
```

### 1. Ingestion Pipeline Details
*   **Custom Markdown Parsing**: Rather than using simple character limit splits, `MarkdownSectionExtractor` parses `.md` destination files based on semantic headings (e.g. `## Top Attractions`, `## Accommodation`).
*   **Metadata Enrichment**: Each chunk is indexed with metadata tags:
    *   `destination` (e.g., Goa, Jaipur, Kerala, Manali)
    *   `section` (e.g., "Food To Try", "Top Attractions", "Best Time To Visit")
*   **Vectorization**: Hugging Face embeds text segments, storing them in a remote Qdrant database.

### 2. Dual-Filtered Retrievals (`TravelContentRetriever`)
To prevent information leak and hallucinations across cities, the `TravelContentRetriever` uses double-filtered metadata queries:
1.  **Destination Matching**: Finds matching records based on extracted destination entities.
2.  **Section Targeting**: Narrows search scope by mapping current intents (classified by `RuleBasedTravelIntentClassifier` such as `FOOD` or `BUDGET`) to corresponding database section values.

```java
Filter destinationFilter = metadataKey("destination").isEqualTo(destination);
Filter sectionFilter = metadataKey("section").isEqualTo(sectionName);
return new And(destinationFilter, sectionFilter);
```

---

## 🎨 Frontend Design System & User Interface

The Next.js frontend is built as a split-grid container dashboard:

```
┌────────────────────────────────────────────────────────────────────────┐
│                      INDIA TRAVEL AI ASSISTANT                         │
├──────────────────────────────────────┬─────────────────────────────────┤
│                                      │                                 │
│  ◀ CHAT PANEL (Sidebar Width: 35%)   │  ▶ ITINERARY VIEW (Residual)    │
│                                      │                                 │
│  - System rules guide                │  - City Title Header & Images   │
│  - Chat history bubbles              │  - Stat pills (Budget, days)    │
│  - Processing typing dots            │  - Boarding pass day-by-day     │
│  - Chat inputs toolbar               │    cards (Sunrise, Moon, Food)  │
│                                      │  - Transport routes grid        │
│                                      │  - Budget chart/Local insights  │
│                                      │                                 │
└──────────────────────────────────────┴─────────────────────────────────┘
```

### 1. Theme Color System (`globals.css`)
Consistent theme setting an India-centric style tone:
*   **Background (`--bg`)**: Midnight Deep Blue (`#0b141f`) representing night skies.
*   **Surface Containers (`--surface`)**: Matte Slate (`#111e30`) for layout cards.
*   **Primary Accent (`--gold`)**: Marigold Indian Gold (`#d4a24c`) symbolizing spices and heritage sites.
*   **Secondary Neon (`--teal`)**: Peacock Deep Green (`#4fb6a8`) for highlighted stats.
*   **Main text font**: Inter (body copy), Georgia/Fraunces (serif for headings), Courier/Plex-mono (monospaced labels).

### 2. UI Components Breakdown

#### A. Chat Panel Component (`ChatPanel.tsx`)
*   **Brand Header**: Animated Plane icons, chat resetting utilities, and guide cards.
*   **Message Normalizer**: Uses a custom regex formatter to clean up raw text (handling bullets like `•` and list spacings) so it displays correctly as structured lists:
```typescript
function normalizeToMarkdown(raw: string): string {
  let text = raw;
  text = text.replace(/\s*•\s*/g, "\n- "); // Replaces bullet chars
  text = text.replace(/([^\n])(\*\*[^*]+:\*\*)/g, "$1\n\n$2"); // Fixes header spacing
  return text.trim();
}
```
*   **ReactMarkdown integration**: Custom rendering overrides ensure HTML hierarchy displays correctly with appropriate padding and highlight colors.

#### B. Itinerary Panel Component (`ItineraryPanel.tsx`)
*   **Destination Header Carousel**: Lazy loaded images from the Unsplash search API.
*   **Transit Grid Tracker**: Visualizes flight, train, or bus travel info including price, operators, and travel times.
*   **Lodging Option Cards**: Renders ratings, amenities, cost, and area information for accommodations.
*   **Budget & Insights**: Displays budget breakdown lists and local travel tips.

#### C. Boarding Pass component (`DayPassCard.tsx`)
Renders day plans styled as a boarding pass. It displays:
*   Sunrise morning activity, afternoon transit, evening spots, recommended meals, and estimated cost.
*   Animated stamp effects for cards entering the page.

---

## 🔗 APIs & Communication Payloads

### Backend POST Interface:
*   **Endpoint**: `/ai/ask/{sessionId}`
*   **Body Content-Type**: `text/plain` (Raw Question prompt string)
*   **Response Content-Type**: `application/json`

### Unified Data Transfer Contract (`AiResponse`):
```json
{
  "type": "ITINERARY", 
  "message": "Itinerary drafted successfully",
  "itinerary": {
    "overview": {
      "destination": "Goa",
      "duration": "3 Days",
      "totalBudget": "₹15,000",
      "groupSize": 2,
      "style": "backpacker"
    },
    "howToReach": [
      {
        "mode": "TRAIN",
        "operator": "Konkan Kanya Express",
        "departure": "Mumbai CSMT",
        "arrival": "Madgaon (MAO)",
        "duration": 10.5,
        "price": 1200.0
      }
    ],
    "hotels": [
      {
        "name": "Zostel Goa",
        "area": "Anjuna",
        "type": "BUDGET",
        "pricePerNight": 800.0,
        "rating": 4.5,
        "amenities": "Free Wifi, Swimming Pool, Cafe"
      }
    ],
    "budgetBreakdown": {
      "Accommodation": "₹2,400",
      "Food": "₹3,000",
      "Local Transport": "₹1,500",
      "Activities": "₹2,000"
    },
    "itinerary": [
      {
        "dayNumber": 1,
        "theme": "Beaches & Shacks",
        "morningActivity": "Anjuna Beach walk and sun bathing.",
        "afternoonActivity": "Visit Chapora Fort.",
        "eveningActivity": "Sunset at Vagator beach, local seafood dinner.",
        "meals": "Fish Thali (₹200)",
        "dailySpend": "₹1,800"
      }
    ],
    "localInsights": [
      "Rent a scooter for cheaper local transport (₹350/day)."
    ],
    "destinationImages": [
      "https://images.unsplash.com/... regular image URL"
    ]
  }
}
```

---

## 📁 Source Code Directory Layout

```
📂 root/
├── 📂 Travel Assistant/                 # BACKEND SERVICE (Spring Boot)
│   ├── 📂 src/main/java/com/n23/foa/travelassistant/
│   │   ├── 📂 agents/                   # Agent declarations and Java tools
│   │   │   ├── 📂 tools/                # Specialized Java tools (Hotel, transport, budget...)
│   │   │   ├── TravelSupervisorAgent.java 
│   │   │   └── TravelKnowledgeAgent.java
│   │   ├── 📂 classifiers/              # LLM System routing classifier
│   │   ├── 📂 config/                   # Configuration classes (RAG, Chat models, proxies...)
│   │   ├── 📂 controller/               # REST Endpoints and Global Handlers
│   │   ├── 📂 dto/                      # Data Transfer Objects (Constraint model, AI contract...)
│   │   ├── 📂 extractors/               # Entity Constraint extraction engines
│   │   ├── 📂 filters/                  # Qdrant double metadata filter configurations
│   │   ├── 📂 ingestion/                # Document parsers and database loaders
│   │   ├── 📂 memory/                   # Session chat storage
│   │   ├── 📂 retrievalAugmentor/       # Custom retrieval RAG inject files
│   │   └── 📂 service/                  # Code implementation layers (Itinerary planner, validator...)
│   ├── pom.xml                          # Dependencies and plugins configuration
│   └── Dockerfile                       # Container definition file
│
└── 📂 frontend/                         # FRONTEND SERVICE (Next.js)
    └── 📂 travel-app-frontend/
        ├── 📂 src/
        │   ├── 📂 app/                  # Route layouts, custom CSS
        │   │   ├── page.tsx
        │   │   └── globals.css
        │   ├── 📂 components/           # UI Dashboards (ChatPanel, Itinerary panel...)
        │   │   ├── ChatPanel.tsx
        │   │   ├── ItineraryPanel.tsx
        │   │   └── DayPassCard.tsx
        │   ├── 📂 lib/                  # Fetch client integrations
        │   │   └── api.ts
        │   └── 📂 types/                # Typescript interface models
        │       └── index.ts
        ├── package.json                 # Node modules configuration
        └── public/                      # Static resources
```

---
<p align="center">Documentation compiled successfully. Designed with ❤️ for the India Travel AI Assistant.</p>

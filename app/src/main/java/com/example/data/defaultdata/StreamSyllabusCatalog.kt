package com.example.data.defaultdata

data class StreamDefinition(
    val id: String,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val defaultCombinations: List<SubjectCombinationPreset>,
    val defaultSubjectNames: List<String>
)

data class SubjectCombinationPreset(
    val name: String,
    val description: String,
    val subjectNames: List<String>
)

object StreamSyllabusCatalog {

    val streams: List<StreamDefinition> = listOf(
        StreamDefinition(
            id = "SCIENCE",
            name = "Science",
            description = "Physics, Chemistry, Maths, Biology, Computer Science",
            iconEmoji = "🔬",
            defaultCombinations = listOf(
                SubjectCombinationPreset("PCM", "Physics, Chemistry, Mathematics, English", listOf("Physics", "Chemistry", "Mathematics", "English")),
                SubjectCombinationPreset("PCB", "Physics, Chemistry, Biology, English", listOf("Physics", "Chemistry", "Biology", "English")),
                SubjectCombinationPreset("PCMB", "Physics, Chemistry, Mathematics, Biology, English", listOf("Physics", "Chemistry", "Mathematics", "Biology", "English")),
                SubjectCombinationPreset("Science + CS", "Physics, Chemistry, Mathematics, Computer Science, English", listOf("Physics", "Chemistry", "Mathematics", "Computer Science", "English")),
                SubjectCombinationPreset("Science + IP", "Physics, Chemistry, Mathematics/Biology, Informatics Practices", listOf("Physics", "Chemistry", "Mathematics", "Informatics Practices", "English"))
            ),
            defaultSubjectNames = listOf("Physics", "Chemistry", "Mathematics", "English")
        ),
        StreamDefinition(
            id = "COMMERCE",
            name = "Commerce",
            description = "Accountancy, Business Studies, Economics, Applied Maths",
            iconEmoji = "💼",
            defaultCombinations = listOf(
                SubjectCombinationPreset("Commerce with Maths", "Accountancy, Business Studies, Economics, Mathematics, English", listOf("Accountancy", "Business Studies", "Economics", "Mathematics", "English")),
                SubjectCombinationPreset("Commerce without Maths", "Accountancy, Business Studies, Economics, Applied Mathematics, English", listOf("Accountancy", "Business Studies", "Economics", "Applied Mathematics", "English")),
                SubjectCombinationPreset("Commerce + IP / CS", "Accountancy, Business Studies, Economics, Informatics Practices, English", listOf("Accountancy", "Business Studies", "Economics", "Informatics Practices", "English")),
                SubjectCombinationPreset("Commerce + Entrepreneurship", "Accountancy, Business Studies, Economics, Entrepreneurship, English", listOf("Accountancy", "Business Studies", "Economics", "Entrepreneurship", "English"))
            ),
            defaultSubjectNames = listOf("Accountancy", "Business Studies", "Economics", "English")
        ),
        StreamDefinition(
            id = "HUMANITIES",
            name = "Humanities / Arts",
            description = "History, Political Science, Geography, Psychology, Sociology",
            iconEmoji = "🎨",
            defaultCombinations = listOf(
                SubjectCombinationPreset("Humanities Core", "History, Political Science, Geography, Economics, English", listOf("History", "Political Science", "Geography", "Economics", "English")),
                SubjectCombinationPreset("Social Sciences & Psychology", "Psychology, Sociology, Political Science, Economics, English", listOf("Psychology", "Sociology", "Political Science", "Economics", "English")),
                SubjectCombinationPreset("Law & Civics", "Legal Studies, Political Science, History, Economics, English", listOf("Legal Studies", "Political Science", "History", "Economics", "English")),
                SubjectCombinationPreset("Civil Services Foundation", "History, Geography, Political Science, Sociology, English", listOf("History", "Geography", "Political Science", "Sociology", "English"))
            ),
            defaultSubjectNames = listOf("History", "Political Science", "Geography", "English")
        ),
        StreamDefinition(
            id = "VOCATIONAL",
            name = "Vocational / Other",
            description = "Information Technology, Financial Markets, Tourism & Media",
            iconEmoji = "🛠️",
            defaultCombinations = listOf(
                SubjectCombinationPreset("IT & Finance", "Information Technology, Financial Markets, Economics, English", listOf("Information Technology", "Financial Markets Management", "Economics", "English")),
                SubjectCombinationPreset("Media & Applied Arts", "Mass Media Studies, Physical Education, English, Computer Science", listOf("Mass Media Studies", "Physical Education", "English", "Computer Science"))
            ),
            defaultSubjectNames = listOf("Information Technology", "Financial Markets Management", "English")
        ),
        StreamDefinition(
            id = "CUSTOM",
            name = "Custom Combination",
            description = "Build a completely tailored mix of subjects for your school",
            iconEmoji = "⚙️",
            defaultCombinations = emptyList(),
            defaultSubjectNames = emptyList()
        )
    )

    fun getStreamById(id: String): StreamDefinition? {
        return streams.find { it.id.equals(id, ignoreCase = true) }
    }

    fun getAvailableSubjectNamesForStream(streamId: String): List<String> {
        return when (streamId.uppercase()) {
            "COMMERCE" -> listOf(
                "Accountancy",
                "Business Studies",
                "Economics",
                "Mathematics",
                "Applied Mathematics",
                "Entrepreneurship",
                "Informatics Practices",
                "Computer Science",
                "English",
                "Physical Education"
            )
            "HUMANITIES" -> listOf(
                "History",
                "Political Science",
                "Geography",
                "Sociology",
                "Psychology",
                "Economics",
                "Legal Studies",
                "English",
                "Hindi",
                "Physical Education",
                "Informatics Practices"
            )
            "VOCATIONAL" -> listOf(
                "Information Technology",
                "Financial Markets Management",
                "Tourism",
                "Artificial Intelligence",
                "Mass Media Studies",
                "Physical Education",
                "English"
            )
            "CUSTOM" -> listOf(
                "Physics", "Chemistry", "Mathematics", "Biology",
                "Accountancy", "Business Studies", "Economics",
                "History", "Political Science", "Geography",
                "Computer Science", "Informatics Practices",
                "Psychology", "Sociology", "Legal Studies",
                "English", "Physical Education"
            )
            else -> listOf( // SCIENCE
                "Physics",
                "Chemistry",
                "Mathematics",
                "Biology",
                "Computer Science",
                "Informatics Practices",
                "English",
                "Physical Education"
            )
        }
    }

    fun getSubjectTemplate(name: String, classLevel: String): DefaultSubjectTemplate {
        val isClass11 = classLevel.contains("11")
        return when (name.trim().lowercase()) {
            // ================= COMMERCE SUBJECTS =================
            "accountancy" -> if (isClass11) {
                DefaultSubjectTemplate(
                    name = "Accountancy",
                    colorHex = "#2563EB",
                    iconName = "account_balance_wallet",
                    chapters = listOf(
                        DefaultChapterTemplate("Introduction to Accounting & Theory Base", 5),
                        DefaultChapterTemplate("Recording Transactions: Journal & Cash Book", 8, true),
                        DefaultChapterTemplate("Ledger & Bank Reconciliation Statement (BRS)", 7, true),
                        DefaultChapterTemplate("Trial Balance and Rectification of Errors", 6),
                        DefaultChapterTemplate("Depreciation, Provisions and Reserves", 7, true),
                        DefaultChapterTemplate("Financial Statements of Sole Proprietorship", 9, true)
                    )
                )
            } else {
                DefaultSubjectTemplate(
                    name = "Accountancy",
                    colorHex = "#2563EB",
                    iconName = "account_balance_wallet",
                    chapters = listOf(
                        DefaultChapterTemplate("Accounting for Partnership: Fundamentals", 6, true),
                        DefaultChapterTemplate("Admission of a Partner", 8, true),
                        DefaultChapterTemplate("Retirement and Death of a Partner", 7, true),
                        DefaultChapterTemplate("Dissolution of Partnership Firm", 6),
                        DefaultChapterTemplate("Accounting for Share Capital", 9, true),
                        DefaultChapterTemplate("Issue and Redemption of Debentures", 6),
                        DefaultChapterTemplate("Financial Statements of a Company & Tools", 5),
                        DefaultChapterTemplate("Accounting Ratios", 7, true),
                        DefaultChapterTemplate("Cash Flow Statement", 8, true)
                    )
                )
            }

            "business studies" -> if (isClass11) {
                DefaultSubjectTemplate(
                    name = "Business Studies",
                    colorHex = "#0D9488",
                    iconName = "business_center",
                    chapters = listOf(
                        DefaultChapterTemplate("Evolution and Fundamentals of Business", 5),
                        DefaultChapterTemplate("Forms of Business Enterprises", 7, true),
                        DefaultChapterTemplate("Public, Private and Global Enterprises", 5),
                        DefaultChapterTemplate("Business Services: Banking, Insurance, Postal", 6),
                        DefaultChapterTemplate("Emerging Modes of Business & Social Responsibility", 5),
                        DefaultChapterTemplate("Sources of Business Finance", 7, true),
                        DefaultChapterTemplate("Small Business and Enterprises", 4),
                        DefaultChapterTemplate("Internal Trade & International Business", 6)
                    )
                )
            } else {
                DefaultSubjectTemplate(
                    name = "Business Studies",
                    colorHex = "#0D9488",
                    iconName = "business_center",
                    chapters = listOf(
                        DefaultChapterTemplate("Nature and Significance of Management", 5),
                        DefaultChapterTemplate("Principles of Management", 6, true),
                        DefaultChapterTemplate("Business Environment", 4),
                        DefaultChapterTemplate("Planning", 5),
                        DefaultChapterTemplate("Organising", 7, true),
                        DefaultChapterTemplate("Staffing", 6),
                        DefaultChapterTemplate("Directing", 7, true),
                        DefaultChapterTemplate("Controlling", 4),
                        DefaultChapterTemplate("Financial Management", 8, true),
                        DefaultChapterTemplate("Financial Markets", 6),
                        DefaultChapterTemplate("Marketing Management", 8, true),
                        DefaultChapterTemplate("Consumer Protection", 4)
                    )
                )
            }

            "economics" -> if (isClass11) {
                DefaultSubjectTemplate(
                    name = "Economics",
                    colorHex = "#D97706",
                    iconName = "trending_up",
                    chapters = listOf(
                        DefaultChapterTemplate("Statistics: Introduction & Collection of Data", 4),
                        DefaultChapterTemplate("Organization & Presentation of Data", 5),
                        DefaultChapterTemplate("Measures of Central Tendency", 7, true),
                        DefaultChapterTemplate("Correlation and Index Numbers", 6),
                        DefaultChapterTemplate("Microeconomics: Introduction & Consumer Equilibrium", 7, true),
                        DefaultChapterTemplate("Demand & Elasticity of Demand", 7, true),
                        DefaultChapterTemplate("Production, Cost and Revenue", 7, true),
                        DefaultChapterTemplate("Producer Equilibrium & Market Forms", 6)
                    )
                )
            } else {
                DefaultSubjectTemplate(
                    name = "Economics",
                    colorHex = "#D97706",
                    iconName = "trending_up",
                    chapters = listOf(
                        DefaultChapterTemplate("National Income and Related Aggregates", 9, true),
                        DefaultChapterTemplate("Money and Banking", 5),
                        DefaultChapterTemplate("Determination of Income and Employment", 8, true),
                        DefaultChapterTemplate("Government Budget and the Economy", 5),
                        DefaultChapterTemplate("Balance of Payments and Foreign Exchange", 5),
                        DefaultChapterTemplate("Development Experience (1947-90) & LPG Reforms", 6),
                        DefaultChapterTemplate("Current Challenges: Human Capital & Rural Dev", 5),
                        DefaultChapterTemplate("Employment and Sustainable Development", 5),
                        DefaultChapterTemplate("Comparative Development with Neighbours", 4)
                    )
                )
            }

            "applied mathematics" -> DefaultSubjectTemplate(
                name = "Applied Mathematics",
                colorHex = "#4F46E5",
                iconName = "functions",
                chapters = listOf(
                    DefaultChapterTemplate("Numbers, Quantification & Numerical Applications", 5),
                    DefaultChapterTemplate("Algebra: Matrices and Determinants", 7, true),
                    DefaultChapterTemplate("Calculus Applications in Commerce & Economics", 8, true),
                    DefaultChapterTemplate("Probability Distributions", 6),
                    DefaultChapterTemplate("Index Numbers and Time Series", 5),
                    DefaultChapterTemplate("Financial Mathematics: Annuity, EMI, Bonds", 8, true),
                    DefaultChapterTemplate("Linear Programming Problems", 5)
                )
            )

            "entrepreneurship" -> DefaultSubjectTemplate(
                name = "Entrepreneurship",
                colorHex = "#EA580C",
                iconName = "lightbulb",
                chapters = listOf(
                    DefaultChapterTemplate("Entrepreneurial Opportunity & Sourcing", 6),
                    DefaultChapterTemplate("Enterprise Planning and Business Plan", 7, true),
                    DefaultChapterTemplate("Enterprise Marketing and Sales Strategy", 6),
                    DefaultChapterTemplate("Enterprise Growth Strategies", 6),
                    DefaultChapterTemplate("Business Arithmetic: Working Capital & Break-even", 8, true),
                    DefaultChapterTemplate("Resource Mobilization and Funding", 5)
                )
            )

            // ================= HUMANITIES / ARTS SUBJECTS =================
            "history" -> DefaultSubjectTemplate(
                name = "History",
                colorHex = "#B45309",
                iconName = "menu_book",
                chapters = listOf(
                    DefaultChapterTemplate("Harappan Archaeology: Bricks, Beads and Bones", 6),
                    DefaultChapterTemplate("Early States & Economies: Kings, Farmers, Towns", 6),
                    DefaultChapterTemplate("Kinship, Caste and Class: Early Societies", 5),
                    DefaultChapterTemplate("Thinkers, Beliefs and Buildings: Cultural Developments", 7, true),
                    DefaultChapterTemplate("Through the Eyes of Travellers", 4),
                    DefaultChapterTemplate("Bhakti-Sufi Traditions", 6),
                    DefaultChapterTemplate("Imperial Capital: Vijayanagara", 7, true),
                    DefaultChapterTemplate("Peasants, Zamindars and the State", 5),
                    DefaultChapterTemplate("Colonialism and the Countryside", 6),
                    DefaultChapterTemplate("Rebels and the Raj: The 1857 Revolt", 7, true),
                    DefaultChapterTemplate("Mahatma Gandhi and the Nationalist Movement", 8, true),
                    DefaultChapterTemplate("Framing the Constitution: A New Era", 6, true)
                )
            )

            "political science" -> DefaultSubjectTemplate(
                name = "Political Science",
                colorHex = "#7C3AED",
                iconName = "policy",
                chapters = listOf(
                    DefaultChapterTemplate("The End of Bipolarity", 6, true),
                    DefaultChapterTemplate("Contemporary Centres of Power", 6),
                    DefaultChapterTemplate("Contemporary South Asia", 5),
                    DefaultChapterTemplate("International Organisations (UN and Agencies)", 5),
                    DefaultChapterTemplate("Security in the Contemporary World", 4),
                    DefaultChapterTemplate("Environment and Natural Resources & Globalisation", 5),
                    DefaultChapterTemplate("Challenges of Nation-Building", 7, true),
                    DefaultChapterTemplate("Era of One-Party Dominance & Planned Development", 6),
                    DefaultChapterTemplate("India's External Relations", 5),
                    DefaultChapterTemplate("Challenges to and Restoration of Congress System", 6),
                    DefaultChapterTemplate("Crisis of Democratic Order & Regional Aspirations", 7, true),
                    DefaultChapterTemplate("Recent Developments in Indian Politics", 6, true)
                )
            )

            "geography" -> DefaultSubjectTemplate(
                name = "Geography",
                colorHex = "#059669",
                iconName = "public",
                chapters = listOf(
                    DefaultChapterTemplate("Human Geography: Nature and Scope", 4),
                    DefaultChapterTemplate("World Population: Distribution, Density and Growth", 5),
                    DefaultChapterTemplate("Human Development", 4),
                    DefaultChapterTemplate("Primary, Secondary, Tertiary & Quaternary Activities", 8, true),
                    DefaultChapterTemplate("Transport, Communication and International Trade", 6),
                    DefaultChapterTemplate("India: Population Distribution, Density & Migration", 5),
                    DefaultChapterTemplate("Human Settlements & Land Resources in India", 5),
                    DefaultChapterTemplate("Water, Mineral and Energy Resources of India", 7, true),
                    DefaultChapterTemplate("Planning and Sustainable Development in Indian Context", 5),
                    DefaultChapterTemplate("Geographical Perspective on Selected Environmental Issues", 4)
                )
            )

            "sociology" -> DefaultSubjectTemplate(
                name = "Sociology",
                colorHex = "#9333EA",
                iconName = "groups",
                chapters = listOf(
                    DefaultChapterTemplate("The Demographic Structure of the Indian Society", 5),
                    DefaultChapterTemplate("Social Institutions: Continuity and Change", 6, true),
                    DefaultChapterTemplate("Patterns of Social Inequality and Exclusion", 7, true),
                    DefaultChapterTemplate("The Challenges of Cultural Diversity", 6),
                    DefaultChapterTemplate("Structural and Cultural Change in India", 6),
                    DefaultChapterTemplate("Change and Development in Rural & Industrial Society", 7, true),
                    DefaultChapterTemplate("Social Movements in India", 6)
                )
            )

            "psychology" -> DefaultSubjectTemplate(
                name = "Psychology",
                colorHex = "#C026D3",
                iconName = "psychology",
                chapters = listOf(
                    DefaultChapterTemplate("Variations in Psychological Attributes", 7, true),
                    DefaultChapterTemplate("Self and Personality", 7, true),
                    DefaultChapterTemplate("Meeting Life Challenges: Stress & Coping", 6),
                    DefaultChapterTemplate("Psychological Disorders", 8, true),
                    DefaultChapterTemplate("Therapeutic Approaches", 7, true),
                    DefaultChapterTemplate("Attitude and Social Cognition", 5),
                    DefaultChapterTemplate("Social Influence and Group Processes", 5)
                )
            )

            "legal studies" -> DefaultSubjectTemplate(
                name = "Legal Studies",
                colorHex = "#1E40AF",
                iconName = "gavel",
                chapters = listOf(
                    DefaultChapterTemplate("Judiciary in India: Hierarchy & Powers", 7, true),
                    DefaultChapterTemplate("Topics in Law: Criminal, Civil, Tort & Contract", 8, true),
                    DefaultChapterTemplate("Arbitration, ADR and Legal Services", 6),
                    DefaultChapterTemplate("Human Rights in India", 6, true),
                    DefaultChapterTemplate("Legal Profession in India", 5),
                    DefaultChapterTemplate("International Context of Law & Treaties", 5)
                )
            )

            // ================= TECH & APPLIED =================
            "computer science" -> DefaultSubjectTemplate(
                name = "Computer Science",
                colorHex = "#0284C7",
                iconName = "terminal",
                chapters = listOf(
                    DefaultChapterTemplate("Python Fundamentals, Flow of Control & Functions", 6),
                    DefaultChapterTemplate("File Handling: Text, Binary & CSV Files", 8, true),
                    DefaultChapterTemplate("Data Structures: Linear Stack implementation", 6, true),
                    DefaultChapterTemplate("Computer Networks: Protocol, Architecture & Topologies", 7, true),
                    DefaultChapterTemplate("Database Management & Advanced SQL Queries", 8, true),
                    DefaultChapterTemplate("Interfacing Python with MySQL", 6, true),
                    DefaultChapterTemplate("Cyber Safety, Laws and Intellectual Property Rights", 4)
                )
            )

            "informatics practices" -> DefaultSubjectTemplate(
                name = "Informatics Practices",
                colorHex = "#0891B2",
                iconName = "dataset",
                chapters = listOf(
                    DefaultChapterTemplate("Data Handling using Pandas: Series & DataFrames", 9, true),
                    DefaultChapterTemplate("Data Visualization: Line, Bar & Histogram in Matplotlib", 7, true),
                    DefaultChapterTemplate("Database Querying using SQL Functions & Grouping", 8, true),
                    DefaultChapterTemplate("Introduction to Computer Networks", 5),
                    DefaultChapterTemplate("Societal Impacts, Digital Footprint & Cyber Crimes", 4)
                )
            )

            "english" -> DefaultSubjectTemplate(
                name = "English",
                colorHex = "#E11D48",
                iconName = "auto_stories",
                chapters = listOf(
                    DefaultChapterTemplate("Reading Comprehension & Note Making", 5),
                    DefaultChapterTemplate("Advanced Writing Skills: Notice, Letter, Article, Report", 7, true),
                    DefaultChapterTemplate("Literature: Core Prose & Stories", 8, true),
                    DefaultChapterTemplate("Literature: Core Poetry & Themes", 6, true),
                    DefaultChapterTemplate("Supplementary Reader: Chapters & Critical Analysis", 7)
                )
            )

            "hindi" -> DefaultSubjectTemplate(
                name = "Hindi",
                colorHex = "#DC2626",
                iconName = "translate",
                chapters = listOf(
                    DefaultChapterTemplate("अपठित बोध (गद्यांश व काव्यांश)", 4),
                    DefaultChapterTemplate("रचनात्मक एवं व्यावहारिक लेखन", 6, true),
                    DefaultChapterTemplate("अंतरा / आरोह: काव्य खंड", 7, true),
                    DefaultChapterTemplate("अंतरा / आरोह: गद्य खंड", 7, true),
                    DefaultChapterTemplate("वितान / अंतराल: पूरक पाठ्यपुस्तक", 5)
                )
            )

            "physical education" -> DefaultSubjectTemplate(
                name = "Physical Education",
                colorHex = "#16A34A",
                iconName = "fitness_center",
                chapters = listOf(
                    DefaultChapterTemplate("Management of Sporting Events", 5),
                    DefaultChapterTemplate("Children & Women in Sports", 5),
                    DefaultChapterTemplate("Yoga as Preventive Measure for Lifestyle Disease", 5),
                    DefaultChapterTemplate("Physical Education & Sports for CWSN", 4),
                    DefaultChapterTemplate("Sports & Nutrition", 5),
                    DefaultChapterTemplate("Test & Measurement in Sports", 5),
                    DefaultChapterTemplate("Physiology & Injuries in Sports", 6, true),
                    DefaultChapterTemplate("Biomechanics & Sports", 5),
                    DefaultChapterTemplate("Psychology & Sports", 5),
                    DefaultChapterTemplate("Training in Sports", 5)
                )
            )

            "information technology" -> DefaultSubjectTemplate(
                name = "Information Technology",
                colorHex = "#2563EB",
                iconName = "computer",
                chapters = listOf(
                    DefaultChapterTemplate("Communication Skills & Self Management", 4),
                    DefaultChapterTemplate("Database Concepts: RDBMS & SQL", 7, true),
                    DefaultChapterTemplate("Web Development: HTML5, CSS & JavaScript", 8, true),
                    DefaultChapterTemplate("Work Integrated Learning & Cyber Ethics", 5)
                )
            )

            "financial markets management" -> DefaultSubjectTemplate(
                name = "Financial Markets Management",
                colorHex = "#059669",
                iconName = "candlestick_chart",
                chapters = listOf(
                    DefaultChapterTemplate("Markets and Financial Instruments", 6),
                    DefaultChapterTemplate("Primary Market Operations & IPOs", 6, true),
                    DefaultChapterTemplate("Secondary Market Trading & Clearing", 7, true),
                    DefaultChapterTemplate("Derivatives Market & Risk Management", 7, true),
                    DefaultChapterTemplate("Financial Statement Analysis & Ratios", 6)
                )
            )

            // Fallback for Mathematics in Class 12 / 11
            "mathematics" -> DefaultSubjectTemplate(
                name = "Mathematics",
                colorHex = "#F59E0B",
                iconName = "calculate",
                chapters = if (isClass11) {
                    listOf(
                        DefaultChapterTemplate("Sets, Relations & Functions", 6),
                        DefaultChapterTemplate("Trigonometric Functions", 7, true),
                        DefaultChapterTemplate("Complex Numbers & Quadratic Equations", 6),
                        DefaultChapterTemplate("Linear Inequalities & Permutations", 6),
                        DefaultChapterTemplate("Binomial Theorem & Sequences", 6),
                        DefaultChapterTemplate("Straight Lines & Conic Sections", 8, true),
                        DefaultChapterTemplate("Limits and Derivatives", 7, true),
                        DefaultChapterTemplate("Statistics and Probability", 6)
                    )
                } else {
                    listOf(
                        DefaultChapterTemplate("Relations and Functions", 5),
                        DefaultChapterTemplate("Inverse Trigonometric Functions", 4),
                        DefaultChapterTemplate("Matrices & Determinants", 7, true),
                        DefaultChapterTemplate("Continuity and Differentiability", 7, true),
                        DefaultChapterTemplate("Application of Derivatives", 7, true),
                        DefaultChapterTemplate("Integrals & Application of Integrals", 9, true),
                        DefaultChapterTemplate("Differential Equations", 6, true),
                        DefaultChapterTemplate("Vector Algebra & 3D Geometry", 8, true),
                        DefaultChapterTemplate("Linear Programming & Probability", 6, true)
                    )
                }
            )

            // Fallback for Physics
            "physics" -> DefaultSubjectTemplate(
                name = "Physics",
                colorHex = "#1A73E8",
                iconName = "speed",
                chapters = if (isClass11) {
                    listOf(
                        DefaultChapterTemplate("Units and Measurements", 4),
                        DefaultChapterTemplate("Motion in a Straight Line & Plane", 7, true),
                        DefaultChapterTemplate("Laws of Motion", 7, true),
                        DefaultChapterTemplate("Work, Energy and Power", 6, true),
                        DefaultChapterTemplate("System of Particles & Rotational Motion", 8, true),
                        DefaultChapterTemplate("Gravitation", 5),
                        DefaultChapterTemplate("Mechanical Properties of Matter & Fluids", 6),
                        DefaultChapterTemplate("Thermodynamics & Kinetic Theory", 7, true),
                        DefaultChapterTemplate("Oscillations and Waves", 7, true)
                    )
                } else {
                    listOf(
                        DefaultChapterTemplate("Electric Charges and Fields", 6, true),
                        DefaultChapterTemplate("Electrostatic Potential and Capacitance", 6),
                        DefaultChapterTemplate("Current Electricity", 7, true),
                        DefaultChapterTemplate("Moving Charges and Magnetism", 7),
                        DefaultChapterTemplate("Magnetism and Matter", 4),
                        DefaultChapterTemplate("Electromagnetic Induction & AC", 7, true),
                        DefaultChapterTemplate("Electromagnetic Waves", 3),
                        DefaultChapterTemplate("Ray Optics and Wave Optics", 9, true),
                        DefaultChapterTemplate("Dual Nature of Radiation & Matter", 5),
                        DefaultChapterTemplate("Atoms and Nuclei", 6, true),
                        DefaultChapterTemplate("Semiconductor Electronics", 7, true)
                    )
                }
            )

            // Fallback for Chemistry
            "chemistry" -> DefaultSubjectTemplate(
                name = "Chemistry",
                colorHex = "#10B981",
                iconName = "science",
                chapters = if (isClass11) {
                    listOf(
                        DefaultChapterTemplate("Some Basic Concepts of Chemistry", 5),
                        DefaultChapterTemplate("Structure of Atom", 6, true),
                        DefaultChapterTemplate("Periodicity & Chemical Bonding", 8, true),
                        DefaultChapterTemplate("Chemical Thermodynamics", 7, true),
                        DefaultChapterTemplate("Equilibrium", 8, true),
                        DefaultChapterTemplate("Redox Reactions", 4),
                        DefaultChapterTemplate("Organic Chemistry: Basic Principles", 9, true),
                        DefaultChapterTemplate("Hydrocarbons", 7, true)
                    )
                } else {
                    listOf(
                        DefaultChapterTemplate("Solutions", 6, true),
                        DefaultChapterTemplate("Electrochemistry", 7, true),
                        DefaultChapterTemplate("Chemical Kinetics", 6, true),
                        DefaultChapterTemplate("The d- and f-Block Elements", 6),
                        DefaultChapterTemplate("Coordination Compounds", 7, true),
                        DefaultChapterTemplate("Haloalkanes and Haloarenes", 6),
                        DefaultChapterTemplate("Alcohols, Phenols and Ethers", 7, true),
                        DefaultChapterTemplate("Aldehydes, Ketones and Carboxylic Acids", 8, true),
                        DefaultChapterTemplate("Amines & Biomolecules", 6)
                    )
                }
            )

            // Fallback for Biology
            "biology" -> DefaultSubjectTemplate(
                name = "Biology",
                colorHex = "#8B5CF6",
                iconName = "eco",
                chapters = if (isClass11) {
                    listOf(
                        DefaultChapterTemplate("The Living World & Classification", 5),
                        DefaultChapterTemplate("Plant Kingdom & Animal Kingdom", 7, true),
                        DefaultChapterTemplate("Morphology and Anatomy of Plants", 6),
                        DefaultChapterTemplate("Cell Structure and Function", 7, true),
                        DefaultChapterTemplate("Plant Physiology", 8, true),
                        DefaultChapterTemplate("Human Physiology", 9, true)
                    )
                } else {
                    listOf(
                        DefaultChapterTemplate("Sexual Reproduction in Flowering Plants", 6, true),
                        DefaultChapterTemplate("Human Reproduction & Reproductive Health", 6),
                        DefaultChapterTemplate("Principles of Inheritance and Variation", 8, true),
                        DefaultChapterTemplate("Molecular Basis of Inheritance", 8, true),
                        DefaultChapterTemplate("Evolution", 5),
                        DefaultChapterTemplate("Human Health and Diseases", 6),
                        DefaultChapterTemplate("Biotechnology: Principles and Applications", 7, true),
                        DefaultChapterTemplate("Ecology and Environment", 7, true)
                    )
                }
            )

            // Generic Custom Subject generator
            else -> createCustomSubjectTemplate(name)
        }
    }

    fun createCustomSubjectTemplate(name: String, colorHex: String = "#38BDF8"): DefaultSubjectTemplate {
        return DefaultSubjectTemplate(
            name = name.trim().ifEmpty { "Custom Subject" },
            colorHex = colorHex,
            iconName = "bookmark",
            chapters = listOf(
                DefaultChapterTemplate("Unit 1: Fundamentals & Theory", 5),
                DefaultChapterTemplate("Unit 2: Core Concepts & Principles", 6, true),
                DefaultChapterTemplate("Unit 3: Applied Topics & Problem Solving", 6, true),
                DefaultChapterTemplate("Unit 4: Advanced Modules & Case Studies", 5),
                DefaultChapterTemplate("Unit 5: Final Revision & Mock Practice", 4)
            )
        )
    }
}

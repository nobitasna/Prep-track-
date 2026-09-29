package com.example.data.defaultdata

data class DefaultExamTemplate(
    val category: String,
    val examName: String,
    val availableYears: List<String>,
    val subjects: List<DefaultSubjectTemplate>
)

data class DefaultSubjectTemplate(
    val name: String,
    val colorHex: String,
    val iconName: String,
    val chapters: List<DefaultChapterTemplate>
)

data class DefaultChapterTemplate(
    val name: String,
    val lectureCount: Int,
    val isImportant: Boolean = false
)

object DefaultSyllabusCatalog {

    val categories = listOf(
        "Medical",
        "Engineering",
        "School / Board",
        "University",
        "Defence",
        "Government Exams",
        "Custom Goal"
    )

    fun getTemplatesForCategory(category: String): List<DefaultExamTemplate> {
        return allTemplates.filter { it.category == category }
    }

    fun getTemplateByName(name: String): DefaultExamTemplate? {
        return allTemplates.find { it.examName == name }
    }

    val allTemplates = listOf(
        // 1. NEET (Medical)
        DefaultExamTemplate(
            category = "Medical",
            examName = "NEET",
            availableYears = listOf("2027", "2028"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Physics",
                    colorHex = "#1A73E8",
                    iconName = "speed",
                    chapters = listOf(
                        DefaultChapterTemplate("Units & Measurements", 4, true),
                        DefaultChapterTemplate("Motion in a Straight Line", 5),
                        DefaultChapterTemplate("Motion in a Plane", 6),
                        DefaultChapterTemplate("Laws of Motion", 7, true),
                        DefaultChapterTemplate("Work, Energy and Power", 6, true),
                        DefaultChapterTemplate("System of Particles & Rotational Motion", 8, true),
                        DefaultChapterTemplate("Gravitation", 5),
                        DefaultChapterTemplate("Mechanical Properties of Solids & Fluids", 6),
                        DefaultChapterTemplate("Thermal Physics & Thermodynamics", 7, true),
                        DefaultChapterTemplate("Oscillations & Waves", 7),
                        DefaultChapterTemplate("Electrostatics & Capacitance", 8, true),
                        DefaultChapterTemplate("Current Electricity", 7, true),
                        DefaultChapterTemplate("Magnetic Effects of Current & Magnetism", 8),
                        DefaultChapterTemplate("Electromagnetic Induction & AC", 6),
                        DefaultChapterTemplate("Ray Optics & Optical Instruments", 8, true),
                        DefaultChapterTemplate("Wave Optics", 5),
                        DefaultChapterTemplate("Modern Physics & Semiconductor Devices", 9, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Chemistry",
                    colorHex = "#10B981",
                    iconName = "science",
                    chapters = listOf(
                        DefaultChapterTemplate("Some Basic Concepts of Chemistry", 5),
                        DefaultChapterTemplate("Structure of Atom", 6, true),
                        DefaultChapterTemplate("Periodic Classification of Elements", 4),
                        DefaultChapterTemplate("Chemical Bonding & Molecular Structure", 8, true),
                        DefaultChapterTemplate("Chemical Thermodynamics", 7, true),
                        DefaultChapterTemplate("Equilibrium (Physical & Chemical)", 8, true),
                        DefaultChapterTemplate("Redox Reactions & Electrochemistry", 7, true),
                        DefaultChapterTemplate("Solutions", 6, true),
                        DefaultChapterTemplate("Chemical Kinetics", 6),
                        DefaultChapterTemplate("Coordination Compounds", 6, true),
                        DefaultChapterTemplate("General Organic Chemistry (GOC)", 9, true),
                        DefaultChapterTemplate("Hydrocarbons", 7),
                        DefaultChapterTemplate("Haloalkanes & Haloarenes", 5),
                        DefaultChapterTemplate("Alcohols, Phenols & Ethers", 6),
                        DefaultChapterTemplate("Aldehydes, Ketones & Carboxylic Acids", 7, true),
                        DefaultChapterTemplate("Amines & Biomolecules", 6)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Biology",
                    colorHex = "#8B5CF6",
                    iconName = "eco",
                    chapters = listOf(
                        DefaultChapterTemplate("The Living World & Biological Classification", 5),
                        DefaultChapterTemplate("Plant Kingdom & Animal Kingdom", 8, true),
                        DefaultChapterTemplate("Morphology & Anatomy of Flowering Plants", 7),
                        DefaultChapterTemplate("Cell: The Unit of Life & Cell Cycle", 7, true),
                        DefaultChapterTemplate("Photosynthesis & Respiration in Plants", 8, true),
                        DefaultChapterTemplate("Plant Growth & Development", 4),
                        DefaultChapterTemplate("Human Physiology: Breathing & Circulation", 8, true),
                        DefaultChapterTemplate("Human Physiology: Excretion & Locomotion", 7),
                        DefaultChapterTemplate("Human Physiology: Neural & Chemical Coordination", 8, true),
                        DefaultChapterTemplate("Sexual Reproduction in Flowering Plants", 6, true),
                        DefaultChapterTemplate("Human Reproduction & Reproductive Health", 6),
                        DefaultChapterTemplate("Principles of Inheritance and Variation", 9, true),
                        DefaultChapterTemplate("Molecular Basis of Inheritance", 9, true),
                        DefaultChapterTemplate("Evolution", 5),
                        DefaultChapterTemplate("Biotechnology: Principles & Processes", 6, true),
                        DefaultChapterTemplate("Ecology & Environment", 7, true)
                    )
                )
            )
        ),

        // 2. JEE Main (Engineering)
        DefaultExamTemplate(
            category = "Engineering",
            examName = "JEE Main",
            availableYears = listOf("2027", "2028"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Physics",
                    colorHex = "#1A73E8",
                    iconName = "speed",
                    chapters = listOf(
                        DefaultChapterTemplate("Kinematics 1D & 2D", 6),
                        DefaultChapterTemplate("Newton's Laws of Motion & Friction", 7, true),
                        DefaultChapterTemplate("Work, Energy, Power & Collisions", 7, true),
                        DefaultChapterTemplate("Rotational Dynamics", 9, true),
                        DefaultChapterTemplate("Gravitation & Fluid Mechanics", 7),
                        DefaultChapterTemplate("SHM & Waves", 8),
                        DefaultChapterTemplate("Thermodynamics & Kinetic Theory", 7, true),
                        DefaultChapterTemplate("Electrostatics & Capacitors", 9, true),
                        DefaultChapterTemplate("Current Electricity & RC Circuits", 7, true),
                        DefaultChapterTemplate("Electromagnetism & EMI", 9, true),
                        DefaultChapterTemplate("Optics (Ray & Wave)", 9, true),
                        DefaultChapterTemplate("Modern Physics & Semiconductors", 8, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Chemistry",
                    colorHex = "#10B981",
                    iconName = "science",
                    chapters = listOf(
                        DefaultChapterTemplate("Mole Concept & Stoichiometry", 5),
                        DefaultChapterTemplate("Atomic Structure", 6),
                        DefaultChapterTemplate("Chemical Bonding", 8, true),
                        DefaultChapterTemplate("Thermodynamics & Thermochemistry", 7, true),
                        DefaultChapterTemplate("Chemical & Ionic Equilibrium", 8, true),
                        DefaultChapterTemplate("Electrochemistry & Kinetics", 8, true),
                        DefaultChapterTemplate("Coordination Compounds", 6, true),
                        DefaultChapterTemplate("General Organic Chemistry (GOC)", 9, true),
                        DefaultChapterTemplate("Hydrocarbons & Reaction Mechanisms", 8, true),
                        DefaultChapterTemplate("Organic Compounds containing O & N", 10, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Mathematics",
                    colorHex = "#F59E0B",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Sets, Relations & Functions", 6),
                        DefaultChapterTemplate("Complex Numbers & Quadratic Equations", 7, true),
                        DefaultChapterTemplate("Matrices and Determinants", 6, true),
                        DefaultChapterTemplate("Permutations, Combinations & Probability", 8, true),
                        DefaultChapterTemplate("Sequence and Series & Binomial Theorem", 7),
                        DefaultChapterTemplate("Limits, Continuity & Differentiability", 8, true),
                        DefaultChapterTemplate("Applications of Derivatives (AOD)", 8, true),
                        DefaultChapterTemplate("Definite & Indefinite Integration", 9, true),
                        DefaultChapterTemplate("Differential Equations & Area Under Curve", 7, true),
                        DefaultChapterTemplate("Coordinate Geometry: Circles & Conics", 10, true),
                        DefaultChapterTemplate("Vectors & 3D Geometry", 9, true)
                    )
                )
            )
        ),

        // 3. School / Board: CBSE Class 10
        DefaultExamTemplate(
            category = "School / Board",
            examName = "CBSE Class 10",
            availableYears = listOf("2026-27", "2027-28"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Science",
                    colorHex = "#10B981",
                    iconName = "science",
                    chapters = listOf(
                        DefaultChapterTemplate("Chemical Reactions & Equations", 4),
                        DefaultChapterTemplate("Acids, Bases and Salts", 5),
                        DefaultChapterTemplate("Metals and Non-metals", 5, true),
                        DefaultChapterTemplate("Carbon and its Compounds", 6, true),
                        DefaultChapterTemplate("Life Processes", 7, true),
                        DefaultChapterTemplate("Control and Coordination", 5),
                        DefaultChapterTemplate("How do Organisms Reproduce?", 6, true),
                        DefaultChapterTemplate("Heredity and Evolution", 5),
                        DefaultChapterTemplate("Light - Reflection and Refraction", 7, true),
                        DefaultChapterTemplate("The Human Eye & Colorful World", 4),
                        DefaultChapterTemplate("Electricity", 6, true),
                        DefaultChapterTemplate("Magnetic Effects of Electric Current", 5, true),
                        DefaultChapterTemplate("Our Environment", 3)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Mathematics",
                    colorHex = "#1A73E8",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Real Numbers", 3),
                        DefaultChapterTemplate("Polynomials", 4),
                        DefaultChapterTemplate("Pair of Linear Equations in Two Variables", 5),
                        DefaultChapterTemplate("Quadratic Equations", 5, true),
                        DefaultChapterTemplate("Arithmetic Progressions", 5),
                        DefaultChapterTemplate("Triangles", 6, true),
                        DefaultChapterTemplate("Coordinate Geometry", 4),
                        DefaultChapterTemplate("Introduction to Trigonometry", 6, true),
                        DefaultChapterTemplate("Applications of Trigonometry", 4, true),
                        DefaultChapterTemplate("Circles", 4),
                        DefaultChapterTemplate("Areas Related to Circles", 4),
                        DefaultChapterTemplate("Surface Areas and Volumes", 5, true),
                        DefaultChapterTemplate("Statistics & Probability", 5)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Social Science",
                    colorHex = "#F59E0B",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("The Rise of Nationalism in Europe", 5, true),
                        DefaultChapterTemplate("Nationalism in India", 5, true),
                        DefaultChapterTemplate("Resources and Development", 3),
                        DefaultChapterTemplate("Agriculture & Mineral Resources", 5),
                        DefaultChapterTemplate("Manufacturing Industries", 4),
                        DefaultChapterTemplate("Power Sharing & Federalism", 4),
                        DefaultChapterTemplate("Gender, Religion and Caste", 3),
                        DefaultChapterTemplate("Political Parties", 4, true),
                        DefaultChapterTemplate("Development & Sectors of Indian Economy", 5),
                        DefaultChapterTemplate("Money and Credit & Globalization", 5, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "English",
                    colorHex = "#8B5CF6",
                    iconName = "menu_book",
                    chapters = listOf(
                        DefaultChapterTemplate("First Flight: Prose & Poetry", 8),
                        DefaultChapterTemplate("Footprints Without Feet: Stories", 6),
                        DefaultChapterTemplate("Grammar: Tenses, Modals & Voice", 5),
                        DefaultChapterTemplate("Writing Skills: Letters & Analytical Paragraph", 4)
                    )
                )
            )
        ),

        // 4. University: CUET
        DefaultExamTemplate(
            category = "University",
            examName = "CUET",
            availableYears = listOf("2027", "2028"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "General Test",
                    colorHex = "#1A73E8",
                    iconName = "psychology",
                    chapters = listOf(
                        DefaultChapterTemplate("General Mental Ability & Reasoning", 8, true),
                        DefaultChapterTemplate("Numerical Ability & Quantitative Reasoning", 8, true),
                        DefaultChapterTemplate("General Knowledge & Current Affairs", 6),
                        DefaultChapterTemplate("Logical & Analytical Reasoning", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Language & Reading Comprehension",
                    colorHex = "#10B981",
                    iconName = "menu_book",
                    chapters = listOf(
                        DefaultChapterTemplate("Reading Comprehension Passages", 6, true),
                        DefaultChapterTemplate("Vocabulary & Synonyms/Antonyms", 5),
                        DefaultChapterTemplate("Sentence Rearrangement & Correction", 5)
                    )
                )
            )
        ),

        // 5. Defence: NDA
        DefaultExamTemplate(
            category = "Defence",
            examName = "NDA",
            availableYears = listOf("2027", "2028"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Mathematics (Paper I)",
                    colorHex = "#1A73E8",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Algebra & Quadratic Equations", 7),
                        DefaultChapterTemplate("Matrices & Determinants", 5, true),
                        DefaultChapterTemplate("Trigonometry", 7, true),
                        DefaultChapterTemplate("Analytical Geometry 2D and 3D", 8, true),
                        DefaultChapterTemplate("Differential & Integral Calculus", 9, true),
                        DefaultChapterTemplate("Vector Algebra & Statistics", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General Ability Test (GAT)",
                    colorHex = "#8B5CF6",
                    iconName = "shield",
                    chapters = listOf(
                        DefaultChapterTemplate("English Grammar & Comprehension", 8, true),
                        DefaultChapterTemplate("Physics & Chemistry Basics", 7),
                        DefaultChapterTemplate("General Science & Biology", 5),
                        DefaultChapterTemplate("Indian History & Freedom Movement", 6, true),
                        DefaultChapterTemplate("Geography & Indian Polity", 6),
                        DefaultChapterTemplate("Current Events & Defence Awareness", 5)
                    )
                )
            )
        ),

        // 6. Government Exams: UPSC
        DefaultExamTemplate(
            category = "Government Exams",
            examName = "UPSC",
            availableYears = listOf("2027", "2028"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Indian Polity & Governance",
                    colorHex = "#1A73E8",
                    iconName = "gavel",
                    chapters = listOf(
                        DefaultChapterTemplate("Constitutional Framework & Preamble", 6, true),
                        DefaultChapterTemplate("Fundamental Rights & Duties", 7, true),
                        DefaultChapterTemplate("Parliament & State Legislatures", 8, true),
                        DefaultChapterTemplate("Judiciary: Supreme Court & High Courts", 6, true),
                        DefaultChapterTemplate("Constitutional & Statutory Bodies", 5)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "History of India",
                    colorHex = "#F59E0B",
                    iconName = "history_edu",
                    chapters = listOf(
                        DefaultChapterTemplate("Ancient India & Indus Valley", 5),
                        DefaultChapterTemplate("Medieval India & Art/Culture", 6),
                        DefaultChapterTemplate("Modern Indian History (1757-1947)", 9, true),
                        DefaultChapterTemplate("Post-Independence Consolidation", 4)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Geography & Environment",
                    colorHex = "#10B981",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("Physical Geography of World & India", 8, true),
                        DefaultChapterTemplate("Indian River Systems & Climate", 6, true),
                        DefaultChapterTemplate("Ecology, Biodiversity & Climate Change", 7, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Indian Economy & Development",
                    colorHex = "#8B5CF6",
                    iconName = "trending_up",
                    chapters = listOf(
                        DefaultChapterTemplate("National Income & Macroeconomics", 6, true),
                        DefaultChapterTemplate("Monetary Policy & Banking", 6, true),
                        DefaultChapterTemplate("Fiscal Policy & Budgeting", 5, true),
                        DefaultChapterTemplate("Agriculture & Industry Reforms", 6)
                    )
                )
            )
        ),

        // 7. Custom Goal
        DefaultExamTemplate(
            category = "Custom Goal",
            examName = "Custom Exam",
            availableYears = listOf("2027", "Ongoing"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Core Subject 1",
                    colorHex = "#1A73E8",
                    iconName = "school",
                    chapters = listOf(
                        DefaultChapterTemplate("Module 1: Foundations", 5, true),
                        DefaultChapterTemplate("Module 2: Advanced Topics", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Core Subject 2",
                    colorHex = "#10B981",
                    iconName = "edit_note",
                    chapters = listOf(
                        DefaultChapterTemplate("Chapter A: Core Concepts", 5, true),
                        DefaultChapterTemplate("Chapter B: Practical Applications", 5)
                    )
                )
            )
        )
    )
}

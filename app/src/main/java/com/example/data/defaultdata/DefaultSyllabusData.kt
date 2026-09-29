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
        "Government Exams",
        "Defence",
        "University",
        "Custom Goal"
    )

    fun getTemplatesForCategory(category: String): List<DefaultExamTemplate> {
        return allTemplates.filter { it.category.equals(category, ignoreCase = true) }
    }

    fun getTemplateByName(name: String): DefaultExamTemplate? {
        return allTemplates.find { it.examName.equals(name, ignoreCase = true) }
    }

    val allTemplates: List<DefaultExamTemplate> = listOf(
        // ==========================================
        // 1. MEDICAL: NEET
        // ==========================================
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
                        DefaultChapterTemplate("Rotational Motion", 8, true),
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

        // ==========================================
        // 2. ENGINEERING: JEE Main & JEE Advanced
        // ==========================================
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
        DefaultExamTemplate(
            category = "Engineering",
            examName = "JEE Advanced",
            availableYears = listOf("2027", "2028"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Physics",
                    colorHex = "#1A73E8",
                    iconName = "speed",
                    chapters = listOf(
                        DefaultChapterTemplate("General Physics & Error Analysis", 5),
                        DefaultChapterTemplate("Mechanics: Rotation, Rolling & Collisions", 10, true),
                        DefaultChapterTemplate("Thermal Physics & Heat Transfer", 8, true),
                        DefaultChapterTemplate("Electricity and Magnetism (Advanced)", 11, true),
                        DefaultChapterTemplate("Wave Optics & Polarization", 7),
                        DefaultChapterTemplate("Modern Physics, Nuclear & Photoelectric", 9, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Chemistry",
                    colorHex = "#10B981",
                    iconName = "science",
                    chapters = listOf(
                        DefaultChapterTemplate("Physical Chemistry: Thermodynamics & Kinetics", 9, true),
                        DefaultChapterTemplate("Inorganic Chemistry: p, d, f-block & Coordination", 10, true),
                        DefaultChapterTemplate("Organic Chemistry: Stereochemistry & Reaction Mechanisms", 11, true),
                        DefaultChapterTemplate("Qualitative Salt Analysis & Metallurgy", 7, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Mathematics",
                    colorHex = "#F59E0B",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Algebra: Complex Numbers, Matrices & Prob.", 10, true),
                        DefaultChapterTemplate("Calculus: Limits, AOD & Multi-Integral", 12, true),
                        DefaultChapterTemplate("Coordinate Geometry: Parabola, Ellipse & Hyperbola", 9, true),
                        DefaultChapterTemplate("Vectors & 3D Geometry (Advanced)", 8, true)
                    )
                )
            )
        ),

        // ==========================================
        // 3. SCHOOL / BOARD (Class 8, 9, 10, 11, 12)
        // ==========================================
        DefaultExamTemplate(
            category = "School / Board",
            examName = "CBSE Class 12",
            availableYears = listOf("2026-27", "2027-28"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Physics",
                    colorHex = "#1A73E8",
                    iconName = "speed",
                    chapters = listOf(
                        DefaultChapterTemplate("Electric Charges and Fields", 6, true),
                        DefaultChapterTemplate("Electrostatic Potential and Capacitance", 6),
                        DefaultChapterTemplate("Current Electricity", 7, true),
                        DefaultChapterTemplate("Moving Charges and Magnetism", 7),
                        DefaultChapterTemplate("Magnetism and Matter", 4),
                        DefaultChapterTemplate("Electromagnetic Induction", 5, true),
                        DefaultChapterTemplate("Alternating Current", 6, true),
                        DefaultChapterTemplate("Electromagnetic Waves", 3),
                        DefaultChapterTemplate("Ray Optics and Optical Instruments", 8, true),
                        DefaultChapterTemplate("Wave Optics", 6),
                        DefaultChapterTemplate("Dual Nature of Radiation and Matter", 5),
                        DefaultChapterTemplate("Atoms and Nuclei", 6, true),
                        DefaultChapterTemplate("Semiconductor Electronics", 7, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Chemistry",
                    colorHex = "#10B981",
                    iconName = "science",
                    chapters = listOf(
                        DefaultChapterTemplate("Solutions", 6, true),
                        DefaultChapterTemplate("Electrochemistry", 7, true),
                        DefaultChapterTemplate("Chemical Kinetics", 6, true),
                        DefaultChapterTemplate("The d- and f-Block Elements", 6),
                        DefaultChapterTemplate("Coordination Compounds", 7, true),
                        DefaultChapterTemplate("Haloalkanes and Haloarenes", 6),
                        DefaultChapterTemplate("Alcohols, Phenols and Ethers", 7, true),
                        DefaultChapterTemplate("Aldehydes, Ketones and Carboxylic Acids", 8, true),
                        DefaultChapterTemplate("Amines", 5),
                        DefaultChapterTemplate("Biomolecules", 5)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Mathematics",
                    colorHex = "#F59E0B",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Relations and Functions", 5),
                        DefaultChapterTemplate("Inverse Trigonometric Functions", 4),
                        DefaultChapterTemplate("Matrices", 5, true),
                        DefaultChapterTemplate("Determinants", 5, true),
                        DefaultChapterTemplate("Continuity and Differentiability", 7, true),
                        DefaultChapterTemplate("Application of Derivatives", 7, true),
                        DefaultChapterTemplate("Integrals", 9, true),
                        DefaultChapterTemplate("Application of Integrals", 5),
                        DefaultChapterTemplate("Differential Equations", 6, true),
                        DefaultChapterTemplate("Vector Algebra", 5),
                        DefaultChapterTemplate("Three Dimensional Geometry", 7, true),
                        DefaultChapterTemplate("Linear Programming", 4),
                        DefaultChapterTemplate("Probability", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Biology",
                    colorHex = "#8B5CF6",
                    iconName = "eco",
                    chapters = listOf(
                        DefaultChapterTemplate("Sexual Reproduction in Flowering Plants", 6, true),
                        DefaultChapterTemplate("Human Reproduction", 6, true),
                        DefaultChapterTemplate("Reproductive Health", 4),
                        DefaultChapterTemplate("Principles of Inheritance and Variation", 8, true),
                        DefaultChapterTemplate("Molecular Basis of Inheritance", 8, true),
                        DefaultChapterTemplate("Evolution", 5),
                        DefaultChapterTemplate("Human Health and Disease", 6, true),
                        DefaultChapterTemplate("Microbes in Human Welfare", 4),
                        DefaultChapterTemplate("Biotechnology: Principles and Processes", 6, true),
                        DefaultChapterTemplate("Biotechnology and its Applications", 5),
                        DefaultChapterTemplate("Organisms and Populations", 5),
                        DefaultChapterTemplate("Ecosystem & Biodiversity", 6, true)
                    )
                )
            )
        ),
        DefaultExamTemplate(
            category = "School / Board",
            examName = "CBSE Class 11",
            availableYears = listOf("2026-27", "2027-28"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Physics",
                    colorHex = "#1A73E8",
                    iconName = "speed",
                    chapters = listOf(
                        DefaultChapterTemplate("Units and Measurements", 4),
                        DefaultChapterTemplate("Motion in a Straight Line", 5),
                        DefaultChapterTemplate("Motion in a Plane", 6),
                        DefaultChapterTemplate("Laws of Motion", 7, true),
                        DefaultChapterTemplate("Work, Energy and Power", 6, true),
                        DefaultChapterTemplate("System of Particles and Rotational Motion", 8, true),
                        DefaultChapterTemplate("Gravitation", 5),
                        DefaultChapterTemplate("Mechanical Properties of Solids and Fluids", 6),
                        DefaultChapterTemplate("Thermal Properties of Matter & Thermodynamics", 7, true),
                        DefaultChapterTemplate("Kinetic Theory of Gases", 4),
                        DefaultChapterTemplate("Oscillations and Waves", 7, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Chemistry",
                    colorHex = "#10B981",
                    iconName = "science",
                    chapters = listOf(
                        DefaultChapterTemplate("Some Basic Concepts of Chemistry", 5),
                        DefaultChapterTemplate("Structure of Atom", 6, true),
                        DefaultChapterTemplate("Classification of Elements & Periodicity", 5),
                        DefaultChapterTemplate("Chemical Bonding and Molecular Structure", 8, true),
                        DefaultChapterTemplate("Chemical Thermodynamics", 7, true),
                        DefaultChapterTemplate("Equilibrium", 8, true),
                        DefaultChapterTemplate("Redox Reactions", 4),
                        DefaultChapterTemplate("Organic Chemistry: Some Basic Principles & Techniques", 9, true),
                        DefaultChapterTemplate("Hydrocarbons", 7, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Mathematics",
                    colorHex = "#F59E0B",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Sets & Relations and Functions", 6),
                        DefaultChapterTemplate("Trigonometric Functions", 7, true),
                        DefaultChapterTemplate("Complex Numbers and Quadratic Equations", 6),
                        DefaultChapterTemplate("Linear Inequalities", 4),
                        DefaultChapterTemplate("Permutations and Combinations", 6, true),
                        DefaultChapterTemplate("Binomial Theorem & Sequence and Series", 7),
                        DefaultChapterTemplate("Straight Lines & Conic Sections", 8, true),
                        DefaultChapterTemplate("Introduction to 3D Geometry", 4),
                        DefaultChapterTemplate("Limits and Derivatives", 7, true),
                        DefaultChapterTemplate("Statistics and Probability", 6)
                    )
                )
            )
        ),
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
        DefaultExamTemplate(
            category = "School / Board",
            examName = "CBSE Class 9",
            availableYears = listOf("2026-27", "2027-28"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Science",
                    colorHex = "#10B981",
                    iconName = "science",
                    chapters = listOf(
                        DefaultChapterTemplate("Matter in Our Surroundings", 4),
                        DefaultChapterTemplate("Is Matter Around Us Pure?", 4),
                        DefaultChapterTemplate("Atoms and Molecules", 5, true),
                        DefaultChapterTemplate("Structure of the Atom", 5, true),
                        DefaultChapterTemplate("The Fundamental Unit of Life (Cell)", 6, true),
                        DefaultChapterTemplate("Tissues", 6),
                        DefaultChapterTemplate("Motion", 6, true),
                        DefaultChapterTemplate("Force and Laws of Motion", 6, true),
                        DefaultChapterTemplate("Gravitation", 5, true),
                        DefaultChapterTemplate("Work and Energy", 5, true),
                        DefaultChapterTemplate("Sound", 5),
                        DefaultChapterTemplate("Improvement in Food Resources", 4)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Mathematics",
                    colorHex = "#1A73E8",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Number Systems", 4),
                        DefaultChapterTemplate("Polynomials", 5, true),
                        DefaultChapterTemplate("Coordinate Geometry", 3),
                        DefaultChapterTemplate("Linear Equations in Two Variables", 4),
                        DefaultChapterTemplate("Lines and Angles", 5),
                        DefaultChapterTemplate("Triangles", 6, true),
                        DefaultChapterTemplate("Quadrilaterals", 5),
                        DefaultChapterTemplate("Circles", 5, true),
                        DefaultChapterTemplate("Heron's Formula", 3),
                        DefaultChapterTemplate("Surface Areas and Volumes", 6, true),
                        DefaultChapterTemplate("Statistics", 4)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Social Science",
                    colorHex = "#F59E0B",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("The French Revolution", 5, true),
                        DefaultChapterTemplate("Socialism in Europe & Russian Revolution", 5),
                        DefaultChapterTemplate("Nazism and the Rise of Hitler", 5, true),
                        DefaultChapterTemplate("India: Size and Location", 3),
                        DefaultChapterTemplate("Physical Features of India & Drainage", 5, true),
                        DefaultChapterTemplate("Climate, Vegetation & Wildlife", 4),
                        DefaultChapterTemplate("What is Democracy? Why Democracy?", 4),
                        DefaultChapterTemplate("Constitutional Design & Electoral Politics", 5),
                        DefaultChapterTemplate("The Story of Village Palampur & People as Resource", 4),
                        DefaultChapterTemplate("Poverty as a Challenge & Food Security", 4)
                    )
                )
            )
        ),
        DefaultExamTemplate(
            category = "School / Board",
            examName = "CBSE Class 8",
            availableYears = listOf("2026-27", "2027-28"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Science",
                    colorHex = "#10B981",
                    iconName = "science",
                    chapters = listOf(
                        DefaultChapterTemplate("Crop Production and Management", 4),
                        DefaultChapterTemplate("Microorganisms: Friend and Foe", 4),
                        DefaultChapterTemplate("Coal and Petroleum", 3),
                        DefaultChapterTemplate("Combustion and Flame", 4),
                        DefaultChapterTemplate("Conservation of Plants and Animals", 3),
                        DefaultChapterTemplate("Reproduction in Animals", 5, true),
                        DefaultChapterTemplate("Reaching the Age of Adolescence", 4),
                        DefaultChapterTemplate("Force and Pressure", 5, true),
                        DefaultChapterTemplate("Friction", 4),
                        DefaultChapterTemplate("Sound", 5, true),
                        DefaultChapterTemplate("Chemical Effects of Electric Current", 4),
                        DefaultChapterTemplate("Light", 5, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Mathematics",
                    colorHex = "#1A73E8",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Rational Numbers", 4),
                        DefaultChapterTemplate("Linear Equations in One Variable", 5, true),
                        DefaultChapterTemplate("Understanding Quadrilaterals", 4),
                        DefaultChapterTemplate("Data Handling", 4),
                        DefaultChapterTemplate("Squares and Square Roots", 5),
                        DefaultChapterTemplate("Cubes and Cube Roots", 3),
                        DefaultChapterTemplate("Comparing Quantities (Profit, Loss, Compound Interest)", 6, true),
                        DefaultChapterTemplate("Algebraic Expressions and Identities", 5, true),
                        DefaultChapterTemplate("Mensuration", 6, true),
                        DefaultChapterTemplate("Exponents and Powers", 4),
                        DefaultChapterTemplate("Direct and Inverse Proportions", 4),
                        DefaultChapterTemplate("Factorisation", 5, true),
                        DefaultChapterTemplate("Introduction to Graphs", 3)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Social Science",
                    colorHex = "#F59E0B",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("How, When and Where & From Trade to Territory", 5),
                        DefaultChapterTemplate("Ruling the Countryside & Tribals, Dikus", 4),
                        DefaultChapterTemplate("When People Rebel: 1857 and After", 5, true),
                        DefaultChapterTemplate("Women, Caste and Reform", 4),
                        DefaultChapterTemplate("The Making of the National Movement: 1870s-1947", 5, true),
                        DefaultChapterTemplate("Resources, Land, Soil, Water & Minerals", 5),
                        DefaultChapterTemplate("Agriculture & Industries", 4),
                        DefaultChapterTemplate("The Indian Constitution & Secularism", 4),
                        DefaultChapterTemplate("Parliament and the Making of Laws & Judiciary", 5)
                    )
                )
            )
        ),

        // ==========================================
        // 4. GOVERNMENT EXAMS (UPSC, SSC CGL, SSC CHSL, Banking, Railways)
        // ==========================================
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
                        DefaultChapterTemplate("Fundamental Rights & Duties, DPSP", 8, true),
                        DefaultChapterTemplate("Parliament & State Legislatures", 8, true),
                        DefaultChapterTemplate("Union & State Judiciary (SC & HC)", 6, true),
                        DefaultChapterTemplate("Local Governments (Panchayats & Municipalities)", 5),
                        DefaultChapterTemplate("Constitutional & Statutory Bodies", 6)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "History & Art/Culture",
                    colorHex = "#F59E0B",
                    iconName = "history_edu",
                    chapters = listOf(
                        DefaultChapterTemplate("Ancient India: Indus Valley & Vedic Age", 5),
                        DefaultChapterTemplate("Buddhism, Jainism, Mauryas & Guptas", 6, true),
                        DefaultChapterTemplate("Medieval India: Delhi Sultanate & Mughals", 6),
                        DefaultChapterTemplate("Modern Indian History (1757-1947)", 9, true),
                        DefaultChapterTemplate("Indian Art, Architecture & Literature", 7, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Geography & Environment",
                    colorHex = "#10B981",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("Physical Geography of World & India", 8, true),
                        DefaultChapterTemplate("Indian River Systems, Climate & Soils", 7, true),
                        DefaultChapterTemplate("Ecology, Biodiversity & National Parks", 8, true),
                        DefaultChapterTemplate("Climate Change, Treaties & Environmental Issues", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Indian Economy",
                    colorHex = "#8B5CF6",
                    iconName = "trending_up",
                    chapters = listOf(
                        DefaultChapterTemplate("National Income & Macroeconomics", 6, true),
                        DefaultChapterTemplate("Banking, Monetary Policy & Inflation", 7, true),
                        DefaultChapterTemplate("Government Budgeting & Fiscal Policy", 6, true),
                        DefaultChapterTemplate("Agriculture, Infrastructure & External Sector", 7, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "CSAT (Paper II)",
                    colorHex = "#EC4899",
                    iconName = "psychology",
                    chapters = listOf(
                        DefaultChapterTemplate("Reading Comprehension & Critical Reasoning", 8, true),
                        DefaultChapterTemplate("Quantitative Aptitude & Number System", 8, true),
                        DefaultChapterTemplate("Logical Reasoning & Analytical Ability", 8, true)
                    )
                )
            )
        ),
        DefaultExamTemplate(
            category = "Government Exams",
            examName = "SSC CGL",
            availableYears = listOf("2026-27", "2027"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Quantitative Aptitude",
                    colorHex = "#1A73E8",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Number System & Simplification", 6),
                        DefaultChapterTemplate("Percentage, Profit & Loss, Discount", 8, true),
                        DefaultChapterTemplate("Ratio & Proportion, Mixture & Alligation", 6),
                        DefaultChapterTemplate("Time and Work, Pipes & Cisterns", 6, true),
                        DefaultChapterTemplate("Speed, Time & Distance, Trains & Boats", 6, true),
                        DefaultChapterTemplate("Simple & Compound Interest", 5),
                        DefaultChapterTemplate("Algebra & Quadratic Equations", 7, true),
                        DefaultChapterTemplate("Geometry: Triangles, Circles, Quadrilaterals", 9, true),
                        DefaultChapterTemplate("Mensuration 2D & 3D", 7, true),
                        DefaultChapterTemplate("Trigonometry, Heights & Distances", 8, true),
                        DefaultChapterTemplate("Data Interpretation (Tables, Graphs, Charts)", 6)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General Intelligence & Reasoning",
                    colorHex = "#10B981",
                    iconName = "psychology",
                    chapters = listOf(
                        DefaultChapterTemplate("Analogies & Classification", 5),
                        DefaultChapterTemplate("Coding-Decoding & Series (Number/Alphabet)", 6, true),
                        DefaultChapterTemplate("Blood Relations & Direction Sense", 5),
                        DefaultChapterTemplate("Syllogism, Statement & Conclusions", 6, true),
                        DefaultChapterTemplate("Order, Ranking & Seating Arrangement", 6, true),
                        DefaultChapterTemplate("Venn Diagrams & Dice/Cubes", 4),
                        DefaultChapterTemplate("Non-Verbal: Mirror/Water Images & Paper Folding", 5)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "English Comprehension",
                    colorHex = "#8B5CF6",
                    iconName = "menu_book",
                    chapters = listOf(
                        DefaultChapterTemplate("Reading Comprehension Passages", 6, true),
                        DefaultChapterTemplate("Cloze Test & Para Jumbles", 6, true),
                        DefaultChapterTemplate("Spotting Errors & Sentence Improvement", 7, true),
                        DefaultChapterTemplate("Vocabulary: Synonyms, Antonyms, Spelling", 7, true),
                        DefaultChapterTemplate("Idioms & Phrases, One Word Substitution", 6, true),
                        DefaultChapterTemplate("Active/Passive Voice & Direct/Indirect Speech", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General Awareness",
                    colorHex = "#F59E0B",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("Indian History & Freedom Struggle", 7, true),
                        DefaultChapterTemplate("Geography of India and World", 6),
                        DefaultChapterTemplate("Indian Polity & Constitution", 6, true),
                        DefaultChapterTemplate("Indian Economy & Budget Concepts", 5),
                        DefaultChapterTemplate("General Science: Physics, Chemistry, Biology", 8, true),
                        DefaultChapterTemplate("Current Affairs, Sports, Books & Awards", 6)
                    )
                )
            )
        ),
        DefaultExamTemplate(
            category = "Government Exams",
            examName = "SSC CHSL",
            availableYears = listOf("2026-27", "2027"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Quantitative Aptitude",
                    colorHex = "#1A73E8",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Number Systems & Basic Arithmetic", 5),
                        DefaultChapterTemplate("Percentages, Ratio & Averages", 6, true),
                        DefaultChapterTemplate("Profit, Loss, Simple & Compound Interest", 6, true),
                        DefaultChapterTemplate("Time & Work, Time & Distance", 6, true),
                        DefaultChapterTemplate("Basic Algebra & Elementary Surds", 6, true),
                        DefaultChapterTemplate("Geometry & Mensuration Basics", 7, true),
                        DefaultChapterTemplate("Trigonometry & Statistical Charts", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General Intelligence",
                    colorHex = "#10B981",
                    iconName = "psychology",
                    chapters = listOf(
                        DefaultChapterTemplate("Semantic & Symbolic Analogy", 4),
                        DefaultChapterTemplate("Number Series & Coding-Decoding", 5, true),
                        DefaultChapterTemplate("Blood Relations & Directions", 4),
                        DefaultChapterTemplate("Venn Diagrams & Figural Classification", 4),
                        DefaultChapterTemplate("Paper Folding & Embedded Figures", 4)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "English Language",
                    colorHex = "#8B5CF6",
                    iconName = "menu_book",
                    chapters = listOf(
                        DefaultChapterTemplate("Spotting the Error & Fill in the Blanks", 6, true),
                        DefaultChapterTemplate("Synonyms, Antonyms & Mis-spelt Words", 6, true),
                        DefaultChapterTemplate("Idioms & Phrases, One Word Substitution", 5, true),
                        DefaultChapterTemplate("Sentence Improvement & Active/Passive", 6),
                        DefaultChapterTemplate("Cloze Passage & Comprehension", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General Awareness",
                    colorHex = "#F59E0B",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("Indian History & National Movement", 6),
                        DefaultChapterTemplate("Geography & Environment", 5),
                        DefaultChapterTemplate("Indian Polity & Governance", 5, true),
                        DefaultChapterTemplate("Everyday General Science", 6, true),
                        DefaultChapterTemplate("Current National & International Events", 5)
                    )
                )
            )
        ),
        DefaultExamTemplate(
            category = "Government Exams",
            examName = "Banking (IBPS/SBI PO)",
            availableYears = listOf("2026-27", "2027"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Quantitative Aptitude",
                    colorHex = "#1A73E8",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Data Interpretation (Pie, Bar, Line, Radar)", 8, true),
                        DefaultChapterTemplate("Quadratic Equations & Inequalities", 5, true),
                        DefaultChapterTemplate("Number Series (Missing & Wrong)", 5, true),
                        DefaultChapterTemplate("Simplification & Approximation", 4),
                        DefaultChapterTemplate("Arithmetic: Profit & Loss, SI/CI, Ratio", 8, true),
                        DefaultChapterTemplate("Time & Work, Pipes, Time & Distance", 7, true),
                        DefaultChapterTemplate("Data Sufficiency", 5)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Reasoning Ability",
                    colorHex = "#10B981",
                    iconName = "psychology",
                    chapters = listOf(
                        DefaultChapterTemplate("Puzzles: Box, Floor, Day, Month Based", 9, true),
                        DefaultChapterTemplate("Seating Arrangement: Linear & Circular", 9, true),
                        DefaultChapterTemplate("Syllogisms (Only a few cases)", 5, true),
                        DefaultChapterTemplate("Inequalities & Coding-Decoding", 5),
                        DefaultChapterTemplate("Blood Relations & Direction Sense", 4),
                        DefaultChapterTemplate("Input-Output Machine", 6, true),
                        DefaultChapterTemplate("Critical Reasoning & Statement Assumptions", 6)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "English Language",
                    colorHex = "#8B5CF6",
                    iconName = "menu_book",
                    chapters = listOf(
                        DefaultChapterTemplate("Reading Comprehension (Economy/Banking)", 8, true),
                        DefaultChapterTemplate("Cloze Test & Word Swap", 5, true),
                        DefaultChapterTemplate("Error Detection & Column Matching", 6, true),
                        DefaultChapterTemplate("Para Jumbles & Sentence Connectors", 6)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General & Banking Awareness",
                    colorHex = "#F59E0B",
                    iconName = "account_balance",
                    chapters = listOf(
                        DefaultChapterTemplate("Banking Systems & RBI Functions", 6, true),
                        DefaultChapterTemplate("Monetary Policy & Financial Instruments", 6, true),
                        DefaultChapterTemplate("National Current Affairs & Economic News", 7, true),
                        DefaultChapterTemplate("Financial Terms, Acronyms & HQ", 5)
                    )
                )
            )
        ),
        DefaultExamTemplate(
            category = "Government Exams",
            examName = "Railways (RRB NTPC)",
            availableYears = listOf("2026-27", "2027"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "General Awareness",
                    colorHex = "#F59E0B",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("Current Events of National & International Importance", 6),
                        DefaultChapterTemplate("Games and Sports, Art and Culture of India", 5),
                        DefaultChapterTemplate("Indian Literature, Monuments & Places of India", 5),
                        DefaultChapterTemplate("General Science & Life Science (up to 10th CBSE)", 8, true),
                        DefaultChapterTemplate("History of India and Freedom Struggle", 7, true),
                        DefaultChapterTemplate("Physical, Social and Economic Geography of India", 6),
                        DefaultChapterTemplate("Indian Polity and Constitution", 6, true),
                        DefaultChapterTemplate("General Scientific & Technological Developments", 5)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Mathematics",
                    colorHex = "#1A73E8",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Number System, Decimals & Fractions", 5),
                        DefaultChapterTemplate("LCM, HCF, Ratio and Proportions", 5),
                        DefaultChapterTemplate("Percentages & Mensuration Basics", 6, true),
                        DefaultChapterTemplate("Time and Work, Time and Distance", 6, true),
                        DefaultChapterTemplate("Simple and Compound Interest", 5),
                        DefaultChapterTemplate("Profit and Loss & Elementary Algebra", 6, true),
                        DefaultChapterTemplate("Geometry and Trigonometry", 6, true),
                        DefaultChapterTemplate("Elementary Statistics", 4)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General Intelligence & Reasoning",
                    colorHex = "#10B981",
                    iconName = "psychology",
                    chapters = listOf(
                        DefaultChapterTemplate("Analogies & Completion of Number/Alphabetical Series", 5),
                        DefaultChapterTemplate("Coding and Decoding & Mathematical Operations", 5, true),
                        DefaultChapterTemplate("Relationships & Syllogism", 5, true),
                        DefaultChapterTemplate("Jumbling, Venn Diagrams & Puzzles", 6, true),
                        DefaultChapterTemplate("Data Sufficiency, Statement-Conclusion", 5)
                    )
                )
            )
        ),

        // ==========================================
        // 5. DEFENCE: NDA, CDS, AFCAT
        // ==========================================
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
                        DefaultChapterTemplate("Algebra, Sets & Quadratic Equations", 7),
                        DefaultChapterTemplate("Matrices & Determinants", 5, true),
                        DefaultChapterTemplate("Trigonometry", 7, true),
                        DefaultChapterTemplate("Analytical Geometry 2D and 3D", 8, true),
                        DefaultChapterTemplate("Differential & Integral Calculus", 9, true),
                        DefaultChapterTemplate("Vector Algebra & Probability", 7, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General Ability Test (GAT)",
                    colorHex = "#8B5CF6",
                    iconName = "shield",
                    chapters = listOf(
                        DefaultChapterTemplate("English Grammar & Vocabulary", 8, true),
                        DefaultChapterTemplate("Physics & Chemistry Basics", 7),
                        DefaultChapterTemplate("General Science & Biology", 5),
                        DefaultChapterTemplate("Indian History & Freedom Movement", 6, true),
                        DefaultChapterTemplate("Geography & Indian Polity", 6),
                        DefaultChapterTemplate("Current Events & Defence Awareness", 5)
                    )
                )
            )
        ),
        DefaultExamTemplate(
            category = "Defence",
            examName = "CDS",
            availableYears = listOf("2027", "2028"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Elementary Mathematics",
                    colorHex = "#1A73E8",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Arithmetic: Number System, Ratio, Time & Work", 7),
                        DefaultChapterTemplate("Elementary Number Theory & Unitary Method", 6),
                        DefaultChapterTemplate("Basic Algebra & Remainder Theorem", 6, true),
                        DefaultChapterTemplate("Trigonometry: Simple Identities & Heights", 6, true),
                        DefaultChapterTemplate("Geometry: Lines, Angles, Triangles, Circles", 8, true),
                        DefaultChapterTemplate("Mensuration 2D & 3D & Statistics", 7, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "English Language",
                    colorHex = "#10B981",
                    iconName = "menu_book",
                    chapters = listOf(
                        DefaultChapterTemplate("Reading Comprehension & Ordering of Sentences", 7, true),
                        DefaultChapterTemplate("Spotting Errors & Sentence Correction", 7, true),
                        DefaultChapterTemplate("Synonyms, Antonyms & Idioms", 6, true),
                        DefaultChapterTemplate("Fill in the blanks & Cloze Test", 5)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General Knowledge",
                    colorHex = "#F59E0B",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("Physics, Chemistry & Biology Basics", 8, true),
                        DefaultChapterTemplate("History of India & National Movement", 7, true),
                        DefaultChapterTemplate("Geography of India and Physical Geography", 7, true),
                        DefaultChapterTemplate("Indian Constitution & Polity", 6, true),
                        DefaultChapterTemplate("Current Events & Defence Topics", 6)
                    )
                )
            )
        ),
        DefaultExamTemplate(
            category = "Defence",
            examName = "AFCAT",
            availableYears = listOf("2027", "2028"),
            subjects = listOf(
                DefaultSubjectTemplate(
                    name = "Verbal Ability in English",
                    colorHex = "#1A73E8",
                    iconName = "menu_book",
                    chapters = listOf(
                        DefaultChapterTemplate("Comprehension & Error Detection", 6, true),
                        DefaultChapterTemplate("Sentence Completion & Fill in the Blanks", 5),
                        DefaultChapterTemplate("Synonyms, Antonyms & Testing of Vocabulary", 6, true),
                        DefaultChapterTemplate("Idioms and Phrases", 5)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "General Awareness",
                    colorHex = "#10B981",
                    iconName = "public",
                    chapters = listOf(
                        DefaultChapterTemplate("History, National Movement & Culture", 6),
                        DefaultChapterTemplate("Geography & Environment", 5),
                        DefaultChapterTemplate("Polity & Basic Science", 6),
                        DefaultChapterTemplate("Defence News, Aviation & Sports", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Reasoning & Military Aptitude",
                    colorHex = "#8B5CF6",
                    iconName = "psychology",
                    chapters = listOf(
                        DefaultChapterTemplate("Verbal Skills & Analogies", 5),
                        DefaultChapterTemplate("Spatial Ability & Embedded Figures", 6, true),
                        DefaultChapterTemplate("Rotated Blocks, Pattern Completion & Venn Diagrams", 6, true)
                    )
                ),
                DefaultSubjectTemplate(
                    name = "Numerical Ability",
                    colorHex = "#F59E0B",
                    iconName = "calculate",
                    chapters = listOf(
                        DefaultChapterTemplate("Decimal Fraction, Time and Work, Average", 5),
                        DefaultChapterTemplate("Profit & Loss, Percentage, Ratio & Proportion", 6, true),
                        DefaultChapterTemplate("Simple & Compound Interest, Time & Distance", 6, true)
                    )
                )
            )
        ),

        // ==========================================
        // 6. UNIVERSITY: CUET
        // ==========================================
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

        // ==========================================
        // 7. CUSTOM GOAL
        // ==========================================
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

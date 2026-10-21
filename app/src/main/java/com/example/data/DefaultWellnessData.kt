package com.example.data

object DefaultWellnessData {

    val supportiveQuotes = listOf(
        "Know what matters. Do the next right thing.",
        "I don't have to figure everything out myself.",
        "Your job isn't to solve it instantly. Your job is to find the next step.",
        "Let's reset from here.",
        "A missed task does not mean the day is ruined.",
        "Consistency beats perfection.",
        "Rest is necessary. You're allowed to rest."
    )

    val focusOptions = listOf(
        "Master PCM concepts",
        "Build startups & prototypes",
        "Prepare for NCSC",
        "Build scholarship portfolio",
        "Computer Science & AI",
        "Fitness, mobility & posture",
        "Consistent sleep & self-care"
    )

    val freeTimeOptions = listOf("Morning", "Afternoon", "Evening", "Flexible")
    val durationOptions = listOf(5, 10, 15, 20)

    val nextWeekFocusOptions = listOf(
        "Solve 5 medium Physics numericals before checking solutions",
        "Collect user feedback for Krow prototype",
        "Practice 90-second NCSC pitch for Smart EcoDrain Shield",
        "Review 24h & 3d Error Log entries daily",
        "Protect 9:40 PM wind-down & Night Brain Dump",
        "10-minute movement reset after school"
    )

    val subjectMethods: Map<String, List<String>> = mapOf(
        "Physics" to listOf(
            "Concept",
            "Formula recall",
            "Units & dimensions check",
            "Worked example",
            "Independent problem",
            "DPP / targeted set",
            "Mistake analysis",
            "Spaced revision"
        ),
        "Chemistry Physical" to listOf(
            "Concept",
            "Formula & sign conventions",
            "Worked example",
            "Numerical practice",
            "Error analysis"
        ),
        "Chemistry Organic/Inorganic" to listOf(
            "Understand mechanism/trend",
            "Patterns / reactions / concepts",
            "Active recall without notes",
            "Targeted questions",
            "Mistake review"
        ),
        "Mathematics" to listOf(
            "Concept & theorem",
            "Worked example",
            "Close notes",
            "Independent problem",
            "Mixed practice",
            "Error log"
        ),
        "Computer Science / AI" to listOf(
            "Concept",
            "Recall from memory",
            "Code / apply",
            "Debug edge cases",
            "Explain aloud",
            "Mini challenge"
        )
    )

    val ncscPreparationItems = listOf(
        "Project understanding",
        "Problem statement",
        "Innovation",
        "Prototype",
        "Sustainability",
        "Impact",
        "Presentation",
        "Q&A",
        "Speaking practice",
        "Documents",
        "Materials",
        "Travel/logistics"
    )

    val ncscDailyMissions = listOf(
        "Explain project in 90 seconds",
        "Explain the problem statement clearly",
        "Explain the core innovation & technical mechanism",
        "Answer a likely judge question without notes",
        "Practice crisp 30-second introduction",
        "Practice memorable conclusion & impact summary"
    )

    fun defaultOsTasks(dateString: String): List<OsTaskEntity> = listOf(
        OsTaskEntity(
            title = "Physics practice — Rotational Motion numericals",
            subtitle = "Work on 5 medium questions from Rotational Dynamics. You already completed the one-shot. Start with 2 easy questions, then move to medium. If stuck, use the hint system.",
            whyItMatters = "Your next available focus block is 45 minutes and this is today's highest-priority academic task.",
            domain = "ACADEMIC",
            priorityBucket = "MUST DO",
            isTopThree = true,
            topThreeSlot = 1,
            durationMinutes = 45,
            energyRequired = "NORMAL",
            scheduledTimeSlot = "3:30 PM",
            dateString = dateString,
            subject = "Physics",
            sortOrder = 1
        ),
        OsTaskEntity(
            title = "Krow — Collect user feedback & improve prototype",
            subtitle = "Open your feedback form, document 1 real workflow observation, and refine the prototype screen flow.",
            whyItMatters = "Builds concrete validation evidence for your project portfolio.",
            domain = "PROJECT",
            priorityBucket = "SHOULD DO",
            isTopThree = true,
            topThreeSlot = 2,
            durationMinutes = 45,
            energyRequired = "NORMAL",
            scheduledTimeSlot = "4:30 PM",
            dateString = dateString,
            subject = "Project",
            sortOrder = 2
        ),
        OsTaskEntity(
            title = "Wellness — 10-minute movement + evening routine",
            subtitle = "Complete a 10-minute normal mobility session, hydrate, and do your evening self-care wind-down.",
            whyItMatters = "Keeps your energy steady and posture comfortable after school and coaching.",
            domain = "WELLNESS",
            priorityBucket = "MUST DO",
            isTopThree = true,
            topThreeSlot = 3,
            durationMinutes = 15,
            energyRequired = "LOW",
            scheduledTimeSlot = "5:20 PM",
            dateString = dateString,
            subject = "Wellness",
            sortOrder = 3
        ),
        OsTaskEntity(
            title = "Mathematics — Sequence & Series mixed set",
            subtitle = "Close notes and solve 5 medium problems from Drona sheet.",
            whyItMatters = "Strengthens independent problem setup before tomorrow's class.",
            domain = "ACADEMIC",
            priorityBucket = "SHOULD DO",
            isTopThree = false,
            topThreeSlot = 0,
            durationMinutes = 30,
            energyRequired = "HIGH",
            scheduledTimeSlot = "8:45 PM",
            dateString = dateString,
            subject = "Mathematics",
            sortOrder = 4
        ),
        OsTaskEntity(
            title = "Smart EcoDrain Shield — Practice 90-second NCSC pitch",
            subtitle = "Speak through problem statement, sensor/filtration mechanism, and sustainability workflow.",
            whyItMatters = "Prepares confident Q&A readiness for NCSC presentation.",
            domain = "PROJECT",
            priorityBucket = "CAN POSTPONE",
            isTopThree = false,
            topThreeSlot = 0,
            durationMinutes = 15,
            energyRequired = "LOW",
            scheduledTimeSlot = "9:15 PM",
            dateString = dateString,
            subject = "NCSC",
            sortOrder = 5
        )
    )

    fun defaultTimelineBlocks(): List<TimelineBlockEntity> = listOf(
        TimelineBlockEntity(
            startTime = "7:15",
            endTime = "2:30",
            title = "School",
            subtitle = "Classes, labs & short free-period recall",
            blockType = "FIXED",
            recoveryCategory = "MUST DO",
            durationMinutes = 435,
            sortOrder = 1
        ),
        TimelineBlockEntity(
            startTime = "2:30",
            endTime = "3:00",
            title = "Home — Lunch + decompression",
            subtitle = "Eat, hydrate, and change out of uniform",
            blockType = "MEAL",
            recoveryCategory = "MUST DO",
            durationMinutes = 30,
            sortOrder = 2
        ),
        TimelineBlockEntity(
            startTime = "3:00",
            endTime = "3:25",
            title = "Power reset",
            subtitle = "20-min controlled rest with alarm (no 2-hour naps)",
            blockType = "RESET",
            recoveryCategory = "SHOULD DO",
            durationMinutes = 25,
            sortOrder = 3
        ),
        TimelineBlockEntity(
            startTime = "3:30",
            endTime = "4:15",
            title = "Physics practice",
            subtitle = "45-min Core Study Engine: recall → easy → medium → error log",
            blockType = "FOCUS",
            recoveryCategory = "MUST DO",
            durationMinutes = 45,
            sortOrder = 4
        ),
        TimelineBlockEntity(
            startTime = "4:15",
            endTime = "4:30",
            title = "Break & posture reset",
            subtitle = "Water + 5-min desk & shoulder mobility",
            blockType = "WELLNESS",
            recoveryCategory = "SHOULD DO",
            durationMinutes = 15,
            sortOrder = 5
        ),
        TimelineBlockEntity(
            startTime = "4:30",
            endTime = "5:15",
            title = "Project work",
            subtitle = "Krow / Smart EcoDrain Shield next concrete action",
            blockType = "FOCUS",
            recoveryCategory = "SHOULD DO",
            durationMinutes = 45,
            sortOrder = 6
        ),
        TimelineBlockEntity(
            startTime = "6:00",
            endTime = "8:00",
            title = "Drona / coaching",
            subtitle = "PCM coaching classes & doubt tagging",
            blockType = "FIXED",
            recoveryCategory = "MUST DO",
            durationMinutes = 120,
            sortOrder = 7
        ),
        TimelineBlockEntity(
            startTime = "8:30",
            endTime = "9:00",
            title = "Dinner & family time",
            subtitle = "Unplugged meal & Entertainment Wallet check",
            blockType = "MEAL",
            recoveryCategory = "MUST DO",
            durationMinutes = 30,
            sortOrder = 8
        ),
        TimelineBlockEntity(
            startTime = "9:15",
            endTime = "9:40",
            title = "Light revision + prepare tomorrow",
            subtitle = "Review 3 Error Log items & pack school bag",
            blockType = "FOCUS",
            recoveryCategory = "SHOULD DO",
            durationMinutes = 25,
            sortOrder = 9
        ),
        TimelineBlockEntity(
            startTime = "9:40",
            endTime = "10:15",
            title = "Wind-down & Brain Dump",
            subtitle = "Empty your head, evening skincare & sleep",
            blockType = "WELLNESS",
            recoveryCategory = "MUST DO",
            durationMinutes = 35,
            sortOrder = 10
        )
    )

    fun defaultStudyChapters(): List<StudyChapterEntity> = listOf(
        StudyChapterEntity(
            subject = "Physics",
            chapterName = "Rotational Motion & Moment of Inertia",
            currentTopic = "Torque, Angular Momentum & Rolling Motion",
            weakAreas = "Applying conservation of angular momentum about a moving axis",
            videoResource = "One-Shot Rotational Dynamics Lecture",
            purpose = "Concept",
            oneShotCompleted = true,
            capturedConcept = "Torque equals rate of change of angular momentum about COM or fixed axis.",
            capturedFormula = "τ = Iα , L = Iω + m(r × v_cm)",
            capturedDoubt = "When static friction does zero work during pure rolling on an incline",
            understandingStatus = "YELLOW",
            questionsAttempted = 18,
            questionsSolved = 13,
            questionsPartial = 2,
            questionsWrong = 2,
            questionsSkipped = 1,
            dominantWeakness = "Formula selection"
        ),
        StudyChapterEntity(
            subject = "Chemistry Physical",
            chapterName = "Chemical Thermodynamics",
            currentTopic = "Enthalpy, Entropy & Gibbs Free Energy",
            weakAreas = "Sign conventions in adiabatic vs isothermal reversible expansion",
            videoResource = "Thermodynamics One-Shot",
            purpose = "Weak topic",
            oneShotCompleted = true,
            capturedConcept = "First Law relates internal energy change to heat and work done on the system.",
            capturedFormula = "ΔU = q + w , ΔG = ΔH - TΔS",
            capturedDoubt = "Converting L·atm to Joules quickly in multi-step numericals",
            understandingStatus = "YELLOW",
            questionsAttempted = 15,
            questionsSolved = 11,
            questionsPartial = 2,
            questionsWrong = 2,
            questionsSkipped = 0,
            dominantWeakness = "Calculation"
        ),
        StudyChapterEntity(
            subject = "Chemistry Organic/Inorganic",
            chapterName = "General Organic Chemistry (GOC)",
            currentTopic = "Inductive, Resonance & Hyperconjugation Effects",
            weakAreas = "Comparing carbocation stability when resonance and inductive effects oppose",
            videoResource = "GOC Masterclass",
            purpose = "Concept",
            oneShotCompleted = false,
            capturedConcept = "Resonance delocalization generally dominates over inductive effect except halogens.",
            capturedFormula = "Stability order: Aromatic > Resonance > Hyperconjugation > Inductive",
            capturedDoubt = "Ortho-effect in substituted benzoic acids",
            understandingStatus = "GREEN",
            questionsAttempted = 22,
            questionsSolved = 19,
            questionsPartial = 1,
            questionsWrong = 2,
            questionsSkipped = 0,
            dominantWeakness = "Concept"
        ),
        StudyChapterEntity(
            subject = "Mathematics",
            chapterName = "Complex Numbers & Sequences",
            currentTopic = "Argand Plane, Modulus-Argument & AP/GP Telescoping",
            weakAreas = "Choosing geometric interpretation vs algebraic substitution for locus problems",
            videoResource = "Drona Class Notes + One-Shot",
            purpose = "Full chapter",
            oneShotCompleted = true,
            capturedConcept = "|z - z0| = r represents a circle centered at z0 with radius r.",
            capturedFormula = "z = r(cos θ + i sin θ) , Vn method for telescoping series",
            capturedDoubt = "Triangle inequality equality condition in locus questions",
            understandingStatus = "YELLOW",
            questionsAttempted = 20,
            questionsSolved = 14,
            questionsPartial = 3,
            questionsWrong = 2,
            questionsSkipped = 1,
            dominantWeakness = "Application"
        ),
        StudyChapterEntity(
            subject = "Computer Science / AI",
            chapterName = "Python Data Structures & LLM Agents",
            currentTopic = "Dictionaries, Recursion & Prompt/Tool Structuring",
            weakAreas = "Tracing base cases in recursive tree/list functions",
            videoResource = "CS Lab & AI Builder Docs",
            purpose = "Concept",
            oneShotCompleted = true,
            capturedConcept = "Every recursive call must shrink the state space toward an explicit base case.",
            capturedFormula = "T(n) = T(n-1) + O(1) -> O(n) stack depth",
            capturedDoubt = "Handling JSON schema validation errors gracefully",
            understandingStatus = "GREEN",
            questionsAttempted = 12,
            questionsSolved = 11,
            questionsPartial = 1,
            questionsWrong = 0,
            questionsSkipped = 0,
            dominantWeakness = "Application"
        )
    )

    fun defaultErrorLogs(dateString: String): List<ErrorLogEntity> = listOf(
        ErrorLogEntity(
            subject = "Physics",
            chapter = "Rotational Motion & Moment of Inertia",
            questionSummary = "Solid cylinder rolling down a rough incline of angle θ — find linear acceleration and minimum friction coefficient.",
            mistakeType = "Formula",
            whyIMadeIt = "Used kinetic friction f = μN instead of static friction f_s ≤ μ_s N during pure rolling.",
            correctIdea = "In pure rolling, the point of contact is instantaneously at rest; solve for f_s from τ = Iα and F_net = ma first, then apply f_s ≤ μ_s N.",
            source = "Drona",
            dateString = dateString,
            reviewStage = 1,
            nextReviewLabel = "Review in 24 hours"
        ),
        ErrorLogEntity(
            subject = "Chemistry Physical",
            chapter = "Chemical Thermodynamics",
            questionSummary = "Calculate work done in isothermal reversible expansion of 2 moles of ideal gas from 10L to 20L at 300K.",
            mistakeType = "Calculation",
            whyIMadeIt = "Forgot to multiply by 2.303 when converting natural log (ln) to log10.",
            correctIdea = "w_rev = -2.303 nRT log10(V2/V1); always write R = 8.314 J/mol·K clearly before multiplying.",
            source = "Mission Jeet",
            dateString = dateString,
            reviewStage = 2,
            nextReviewLabel = "Review in 3 days"
        ),
        ErrorLogEntity(
            subject = "Mathematics",
            chapter = "Complex Numbers & Sequences",
            questionSummary = "Find the locus of z if arg((z - 1)/(z + 1)) = π/4.",
            mistakeType = "Approach",
            whyIMadeIt = "Expanded z = x + iy immediately which led to lengthy algebra instead of using inscribed angle geometry.",
            correctIdea = "arg((z - z1)/(z - z2)) = θ represents a major arc of a circle passing through z1 and z2 on one side of the chord.",
            source = "Study Panda",
            dateString = dateString,
            reviewStage = 3,
            nextReviewLabel = "Review in 7 days"
        )
    )

    val practiceQuestionsBank: List<PracticeQuestionItem> = listOf(
        PracticeQuestionItem(
            id = "phys_rot_1",
            subject = "Physics",
            chapter = "Rotational Motion & Moment of Inertia",
            difficulty = "Easy",
            source = "Drona",
            focusSkill = "Formula selection",
            questionText = "A uniform solid disc of mass 4 kg and radius 0.5 m rotates about a perpendicular axis passing through its edge. Find its moment of inertia.",
            givenPrompt = "Mass M = 4 kg, Radius R = 0.5 m, axis is perpendicular to plane at the rim.",
            askedPrompt = "Moment of inertia I about the edge axis.",
            conceptPrompt = "Parallel Axis Theorem: I = I_cm + M d².",
            hint1Concept = "First recall the moment of inertia of a uniform disc about its center of mass perpendicular to its plane.",
            hint2Formula = "Use I_cm = (1/2) M R² and Parallel Axis Theorem I = I_cm + M R² = (3/2) M R².",
            hint3Strategy = "Substitute M = 4 kg and R = 0.5 m into I = 1.5 × M × R².",
            step4FirstStep = "I_cm = 0.5 × 4 × (0.5)² = 0.5 kg·m².",
            step5FullSolution = "I = I_cm + M R² = (3/2) × 4 × (0.25) = 6 × 0.25 = 1.5 kg·m²."
        ),
        PracticeQuestionItem(
            id = "phys_rot_2",
            subject = "Physics",
            chapter = "Rotational Motion & Moment of Inertia",
            difficulty = "Medium",
            source = "Study Panda",
            focusSkill = "Formula selection",
            questionText = "A solid sphere rolls without slipping down a 30° incline. Find the ratio of its translational kinetic energy to its total kinetic energy.",
            givenPrompt = "Solid sphere (I = 2/5 MR²), pure rolling (v = ωR), incline angle 30°.",
            askedPrompt = "Ratio K_trans / K_total.",
            conceptPrompt = "Total kinetic energy in rolling = Translational KE + Rotational KE.",
            hint1Concept = "Notice that the incline angle 30° is extra information! The KE ratio depends only on the shape of the rolling body.",
            hint2Formula = "K_trans = (1/2)Mv² and K_rot = (1/2)Iω² = (1/2)(2/5 MR²)(v/R)² = (1/5)Mv².",
            hint3Strategy = "Add K_trans and K_rot to get K_total in terms of Mv², then divide.",
            step4FirstStep = "K_total = (1/2 + 1/5) Mv² = (7/10) Mv².",
            step5FullSolution = "Ratio = K_trans / K_total = (1/2) / (7/10) = 5 / 7."
        ),
        PracticeQuestionItem(
            id = "phys_rot_3",
            subject = "Physics",
            chapter = "Rotational Motion & Moment of Inertia",
            difficulty = "Hard",
            source = "Mission Jeet",
            focusSkill = "Application",
            questionText = "A thin uniform rod of mass M and length L is hinged at its top end. A small bullet of mass m moving horizontally with velocity v strikes the bottom end and embeds in it. Find the angular velocity just after collision.",
            givenPrompt = "Rod (mass M, length L, hinged at top), bullet (mass m, speed v at distance L from hinge).",
            askedPrompt = "Angular velocity ω immediately after the inelastic collision.",
            conceptPrompt = "Conservation of Angular Momentum about the hinge point.",
            hint1Concept = "Linear momentum is NOT conserved because the hinge exerts an impulsive reaction force, but torque about the hinge is zero.",
            hint2Formula = "L_initial = L_final about hinge => m v L = (I_rod_hinge + I_bullet_hinge) ω.",
            hint3Strategy = "Compute total moment of inertia about the top hinge: I_total = (1/3)ML² + mL².",
            step4FirstStep = "Initial angular momentum about hinge L_i = m v L.",
            step5FullSolution = "m v L = (ML²/3 + mL²) ω  =>  ω = (3 m v) / ((M + 3m) L)."
        ),
        PracticeQuestionItem(
            id = "chem_thermo_1",
            subject = "Chemistry Physical",
            chapter = "Chemical Thermodynamics",
            difficulty = "Medium",
            source = "Drona",
            focusSkill = "Calculation",
            questionText = "For a reaction at 300 K, ΔH = -24 kJ/mol and ΔS = -60 J/(K·mol). Determine ΔG and whether the reaction is spontaneous at 300 K.",
            givenPrompt = "T = 300 K, ΔH = -24 kJ/mol, ΔS = -60 J/(K·mol).",
            askedPrompt = "Value of ΔG in kJ/mol and spontaneity.",
            conceptPrompt = "Gibbs Helmholtz equation ΔG = ΔH - TΔS with unit consistency.",
            hint1Concept = "Watch the units! ΔH is in kJ/mol while ΔS is in J/(K·mol).",
            hint2Formula = "ΔG = ΔH - T × (ΔS / 1000) kJ/mol.",
            hint3Strategy = "Convert TΔS to kJ/mol first, then pay close attention to the double negative sign.",
            step4FirstStep = "TΔS = 300 × (-60) J/mol = -18,000 J/mol = -18 kJ/mol.",
            step5FullSolution = "ΔG = -24 - (-18) = -6 kJ/mol. Since ΔG < 0, the reaction is spontaneous at 300 K."
        ),
        PracticeQuestionItem(
            id = "math_seq_1",
            subject = "Mathematics",
            chapter = "Complex Numbers & Sequences",
            difficulty = "Medium",
            source = "Study Panda",
            focusSkill = "Application",
            questionText = "Find the sum of n terms of the series: 1/(1·3) + 1/(3·5) + 1/(5·7) + ...",
            givenPrompt = "k-th term T_k = 1 / ((2k - 1)(2k + 1)).",
            askedPrompt = "Sum S_n of the first n terms.",
            conceptPrompt = "Method of Differences (Telescoping Series).",
            hint1Concept = "Express the denominator factors' difference in the numerator.",
            hint2Formula = "Notice that (2k + 1) - (2k - 1) = 2, so 1/((2k-1)(2k+1)) = (1/2)[1/(2k-1) - 1/(2k+1)].",
            hint3Strategy = "Write out the first 3 terms and the n-th term to see the inner terms cancel.",
            step4FirstStep = "T_k = (1/2) [ 1/(2k - 1) - 1/(2k + 1) ].",
            step5FullSolution = "S_n = (1/2) [ (1 - 1/3) + (1/3 - 1/5) + ... + (1/(2n-1) - 1/(2n+1)) ] = (1/2)[1 - 1/(2n+1)] = n / (2n + 1)."
        ),
        PracticeQuestionItem(
            id = "cs_ai_1",
            subject = "Computer Science / AI",
            chapter = "Python Data Structures & LLM Agents",
            difficulty = "Easy",
            source = "School",
            focusSkill = "Concept",
            questionText = "What is the time complexity of looking up a key in a Python dictionary on average, and why can mutable lists not be used as dictionary keys?",
            givenPrompt = "Python dict hash table implementation.",
            askedPrompt = "Average lookup time complexity and reason lists cannot be keys.",
            conceptPrompt = "Hashing and immutability requirement for hash keys.",
            hint1Concept = "Think about how Python locates a bucket in a dictionary.",
            hint2Formula = "Average lookup is O(1) via hash(key).",
            hint3Strategy = "Consider what happens if a key's contents change after it is stored in a hash bucket.",
            step4FirstStep = "Average lookup is O(1) because Python computes a hash value directly.",
            step5FullSolution = "Average lookup is O(1). Lists are mutable, so their contents could change after insertion, changing their hash and making the key unfindable in the hash table. Use tuples instead."
        )
    )

    fun defaultProjects(): List<ProjectEntity> = listOf(
        ProjectEntity(
            name = "Krow",
            tagline = "Hyperlocal workflow & discovery prototype",
            pipelineStage = "TEST",
            problemStatement = "Local shopkeepers and student creators struggle to organize simple digital workflows and collect structured feedback without complex software.",
            targetUser = "Local small businesses & student project builders",
            solutionSummary = "A lightweight mobile-first prototype for streamlined local discovery and structured feedback loops.",
            prototypeDetails = "Interactive mobile prototype with core user flows documented.",
            technologyStack = "Kotlin, Jetpack Compose, Python backend prototype",
            myRole = "Founder, Product Designer & Lead Developer",
            testingSummary = "Initial usability walkthroughs in progress; gathering structured user notes.",
            userFeedbackSummary = "Documenting real feedback notes as interviews are completed.",
            impactSummary = "Focusing on qualitative workflow clarity before scaling.",
            githubOrDemoUrl = "https://github.com/astha/krow-prototype",
            competitionTarget = "Student Startup & Innovation Showcase",
            status = "Active Prototype",
            nextConcreteAction = "Open your feedback form and record 3 structured observations from 1 user walkthrough.",
            evProblemDocumented = true,
            evPrototypeExists = true,
            evDemoAvailable = true,
            evGithubAvailable = true,
            evUserFeedback = false,
            evTestingEvidence = false,
            evPresentation = false,
            evImpactEvidence = false,
            evCompetitionSubmission = false
        ),
        ProjectEntity(
            name = "Smart EcoDrain Shield",
            tagline = "Sustainable storm-drain blockage prevention & monitoring system",
            pipelineStage = "EVIDENCE",
            problemStatement = "Urban waterlogging caused by solid waste clogging drainage inlets during heavy rain.",
            targetUser = "Municipal bodies, school campuses & residential neighborhoods",
            solutionSummary = "Multi-stage angled filtration grate with overflow bypass and low-cost water-level alert mechanism.",
            prototypeDetails = "Physical working scale model + circuit schematic.",
            technologyStack = "Embedded sensors, mechanical filtration design, environmental engineering",
            myRole = "Lead Student Researcher & Builder",
            testingSummary = "Water-flow rate and debris separation tested on scale model.",
            userFeedbackSummary = "Science mentor feedback incorporated into slide deck.",
            impactSummary = "Demonstrates prevents clogging while maintaining water flow in model trials.",
            githubOrDemoUrl = "Local project binder & demonstration video",
            competitionTarget = "NCSC (National Children's Science Congress)",
            status = "Preparing NCSC Presentation",
            nextConcreteAction = "Practice explaining the innovation and sustainability metrics in 90 seconds.",
            evProblemDocumented = true,
            evPrototypeExists = true,
            evDemoAvailable = true,
            evGithubAvailable = false,
            evUserFeedback = true,
            evTestingEvidence = true,
            evPresentation = true,
            evImpactEvidence = false,
            evCompetitionSubmission = false
        ),
        ProjectEntity(
            name = "AI Study & Concept Helper Tools",
            tagline = "Hint-first active recall micro-tools for STEM students",
            pipelineStage = "MVP",
            problemStatement = "Standard AI chatbots give away full answers immediately, hurting deep problem-solving skills in Physics and Maths.",
            targetUser = "Class 11 & 12 PCM students",
            solutionSummary = "Stepwise hint-ladder prompts and active recall checklists that guide students to the next step instead of spoiling the solution.",
            prototypeDetails = "Prompt templates and interactive study workflow module.",
            technologyStack = "Gemini API, Kotlin, Structured JSON Prompts",
            myRole = "Creator & Prompt Architect",
            testingSummary = "Self-testing on daily Physics and Maths problem sets.",
            userFeedbackSummary = "Personal dogfooding notes logged.",
            impactSummary = "Helps build persistence on medium/hard numericals.",
            githubOrDemoUrl = "",
            competitionTarget = "AI Hackathon / Portfolio",
            status = "Building MVP",
            nextConcreteAction = "Document 3 before/after examples of hint-based problem solving for the GitHub README.",
            evProblemDocumented = true,
            evPrototypeExists = true,
            evDemoAvailable = false,
            evGithubAvailable = false,
            evUserFeedback = false,
            evTestingEvidence = false,
            evPresentation = false,
            evImpactEvidence = false,
            evCompetitionSubmission = false
        )
    )

    fun defaultProfileAchievements(): List<ProfileAchievementEntity> = listOf(
        ProfileAchievementEntity(
            category = "PROJECTS",
            title = "Smart EcoDrain Shield — Science & Sustainability Prototype",
            dateString = "2026",
            organization = "School Science & NCSC Track",
            role = "Lead Student Researcher",
            whatIDid = "Designed and built a scale prototype to prevent storm-drain clogging while preserving water flow.",
            resultSummary = "Completed working physical model and technical documentation.",
            impactSummary = "Prepared for NCSC presentation and local campus sustainability demonstration.",
            evidenceNote = "Prototype photos, flow test log, and presentation slides.",
            linkUrl = ""
        ),
        ProfileAchievementEntity(
            category = "PROJECTS",
            title = "Krow — Product & Prototype Development",
            dateString = "2026",
            organization = "Independent Student Startup Project",
            role = "Founder & Developer",
            whatIDid = "Defined problem statement, designed UI flows, and built initial functional prototype.",
            resultSummary = "MVP ready for structured user testing.",
            impactSummary = "Building real-world software engineering and user validation skills.",
            evidenceNote = "Prototype build & problem documentation.",
            linkUrl = ""
        ),
        ProfileAchievementEntity(
            category = "ACADEMICS",
            title = "Class 11 PCM + Computer Science Track",
            dateString = "2026",
            organization = "High School & Drona Coaching",
            role = "Student",
            whatIDid = "Pursuing Physics, Chemistry, Mathematics, and Computer Science with structured error-log revision.",
            resultSummary = "Consistent chapter progression and problem-solving log.",
            impactSummary = "Strong conceptual foundation for engineering & CS undergraduate studies.",
            evidenceNote = "Academic transcripts & study portfolio.",
            linkUrl = ""
        ),
        ProfileAchievementEntity(
            category = "TECHNICAL SKILLS",
            title = "Kotlin, Python & AI Application Prototyping",
            dateString = "2026",
            organization = "Self-Directed & CS Curriculum",
            role = "Developer",
            whatIDid = "Building Android applications, Python scripts, and structured LLM workflows.",
            resultSummary = "Functional personal tools and project prototypes.",
            impactSummary = "Applied software development across mobile and AI domains.",
            evidenceNote = "Source code repositories.",
            linkUrl = ""
        )
    )

    fun defaultOpportunities(): List<OpportunityEntity> = listOf(
        OpportunityEntity(
            name = "National Children's Science Congress (NCSC)",
            category = "Competitions",
            country = "India",
            organization = "NCSTC, Department of Science & Technology",
            eligibility = "Students aged 10–17 years (Senior group: 14–17)",
            deadline = "Verify regional/state schedule",
            funding = "Recognition, mentorship & national showcase",
            applicationRequirements = "Project report, logbook, abstract, oral presentation & poster/model",
            officialSource = "Official NCSC State Coordinator / ncsc-india",
            status = "Preparing",
            notes = "Presenting Smart EcoDrain Shield. Focus on 90-second pitch and judge Q&A.",
            isVerified = true,
            lastVerifiedDate = "Verified by user"
        ),
        OpportunityEntity(
            name = "IRIS National Fair (Initiative for Research & Innovation in STEM)",
            category = "Research",
            country = "India / International (ISEF track)",
            organization = " Broadcom / DST / Exsteme",
            eligibility = "Class 5–12 students with original research/engineering projects",
            deadline = "Unverified — check official portal for current cycle",
            funding = "National finalist mentorship & international representation",
            applicationRequirements = "Research synopsis, data logbook, prototype evidence & safety forms",
            officialSource = "https://www.irisnationalfair.org",
            status = "Researching",
            notes = "Verify exact submission window and align Smart EcoDrain Shield testing data.",
            isVerified = false,
            lastVerifiedDate = "Unverified — verify on official portal"
        ),
        OpportunityEntity(
            name = "Undergraduate CS & Engineering Merit Scholarships Track",
            category = "Scholarships",
            country = "India & Global",
            organization = "University Merit & STEM Foundation Programs",
            eligibility = "High school seniors / Class 11–12 portfolio builders",
            deadline = "Unverified — long-term Class 12 application window",
            funding = "Varies by institution (need & merit based)",
            applicationRequirements = "Academic transcripts, documented projects, leadership evidence, essays & recommendations",
            officialSource = "Official University Admissions Portals",
            status = "Saved",
            notes = "Focus right now in Class 11 is building real project evidence and strong PCM grades.",
            isVerified = false,
            lastVerifiedDate = "Unverified — verify on official portal"
        )
    )

    fun defaultHabits(moveDuration: Int = 10, windDownTime: String = "9:40 PM"): List<DailyHabitEntity> = listOf(
        DailyHabitEntity(
            category = "HYDRATE",
            title = "Hydrate",
            subtitle = "Steady water breaks through school & study",
            emoji = "💧",
            completedToday = false,
            sortOrder = 1
        ),
        DailyHabitEntity(
            category = "MOVE",
            title = "Movement & Mobility",
            subtitle = "$moveDuration min personalized session",
            emoji = "🧘",
            completedToday = false,
            sortOrder = 2
        ),
        DailyHabitEntity(
            category = "MEALS",
            title = "Nourishing Meals",
            subtitle = "Regular breakfast, after-school lunch & dinner",
            emoji = "🍲",
            completedToday = false,
            sortOrder = 3
        ),
        DailyHabitEntity(
            category = "POSTURE",
            title = "Posture & Screen Breaks",
            subtitle = "Relax shoulders & look away every 45 min",
            emoji = "🪑",
            completedToday = false,
            sortOrder = 4
        ),
        DailyHabitEntity(
            category = "GLOW",
            title = "Self-Care & Grooming",
            subtitle = "Morning & evening skincare + hygiene",
            emoji = "✨",
            completedToday = false,
            sortOrder = 5
        ),
        DailyHabitEntity(
            category = "SLEEP",
            title = "Sleep & Wind-Down",
            subtitle = "Brain dump & wind-down at $windDownTime",
            emoji = "😴",
            completedToday = false,
            sortOrder = 6
        ),
        DailyHabitEntity(
            category = "CONFIDENCE",
            title = "Confidence Check-In",
            subtitle = "How did you show up today?",
            emoji = "🌸",
            completedToday = false,
            sortOrder = 7
        )
    )

    fun defaultSkincareSteps(): List<SkincareStepEntity> = listOf(
        // Morning
        SkincareStepEntity(period = "MORNING", title = "Wash face gently", completedToday = false, sortOrder = 1),
        SkincareStepEntity(period = "MORNING", title = "Moisturizer & Sunscreen", completedToday = false, sortOrder = 2),
        SkincareStepEntity(period = "MORNING", title = "Brush teeth & hygiene", completedToday = false, sortOrder = 3),
        SkincareStepEntity(period = "MORNING", title = "Hair / neat grooming", completedToday = false, sortOrder = 4),
        SkincareStepEntity(period = "MORNING", title = "Morning water + 3-min posture reach", completedToday = false, sortOrder = 5),
        // Evening
        SkincareStepEntity(period = "EVENING", title = "Cleanse face gently", completedToday = false, sortOrder = 1),
        SkincareStepEntity(period = "EVENING", title = "Moisturizer", completedToday = false, sortOrder = 2),
        SkincareStepEntity(period = "EVENING", title = "Brush teeth & evening hygiene", completedToday = false, sortOrder = 3),
        SkincareStepEntity(period = "EVENING", title = "Prepare tomorrow's uniform & bag", completedToday = false, sortOrder = 4),
        SkincareStepEntity(period = "EVENING", title = "Wind-down away from bright screens", completedToday = false, sortOrder = 5)
    )

    fun defaultHydrationReminders(): List<HydrationReminderEntity> = listOf(
        HydrationReminderEntity(timeLabel = "7:00 AM", hour24 = 7, minute = 0, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "11:00 AM", hour24 = 11, minute = 0, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "2:35 PM", hour24 = 14, minute = 35, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "4:20 PM", hour24 = 16, minute = 20, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "6:00 PM", hour24 = 18, minute = 0, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "8:30 PM", hour24 = 20, minute = 30, isEnabled = true, completedToday = false)
    )

    val movementRoutines: List<MovementRoutine> = listOf(
        MovementRoutine(
            id = "quick_reset_5",
            title = "Quick Reset — 5 Min Mobility & Posture",
            category = "QUICK RESET",
            durationMinutes = 5,
            difficulty = "All Levels",
            focusTags = "Posture • Energy • Desk Reset",
            description = "Fast 5-minute reset between Physics/Maths blocks or right after school.",
            steps = listOf(
                ExerciseStep("20-20-20 Eye & Jaw Softening", "Look far away from your desk and unclench your jaw and shoulders.", 60, "Slow nasal inhale, long exhale."),
                ExerciseStep("Thoracic & Chest Opener", "Interlace fingers behind your back or open arms wide to counteract desk slouching.", 75, "Expand your ribcage gently."),
                ExerciseStep("Neck & Shoulder Rolls", "Slowly circle shoulders backward 8 times, then glide ears toward shoulders.", 75, "Let backpack and desk tension melt."),
                ExerciseStep("Hip Flexor & Hamstring Wake-Up", "Step into a gentle lunge on each side to release tight hips from sitting.", 90, "Stand tall and grounded.")
            )
        ),
        MovementRoutine(
            id = "normal_12_flow",
            title = "Normal Session — 12 Min Strength & Mobility",
            category = "NORMAL",
            durationMinutes = 12,
            difficulty = "Beginner",
            focusTags = "Capability • Posture • Mobility",
            description = "Balanced daily movement to feel strong, energetic, and upright without exhaustion.",
            steps = listOf(
                ExerciseStep("Sunrise Spine & Shoulder Flow", "Sweep arms overhead and flow through gentle standing cat-cow movements.", 120, "Move smoothly with your breath."),
                ExerciseStep("Bodyweight Squats", "Sit hips back comfortably and press through your heels to stand tall.", 120, "Focus on control and steady knees."),
                ExerciseStep("Wall or Incline Push-Ups", "Controlled upper-body pressing to build shoulder and core stability.", 120, "Keep your neck long."),
                ExerciseStep("Glute Bridges & Core Bracing", "Lie on your back, lift hips steadily, and hold for 2 seconds at the top.", 120, "Supports lower back during long study hours."),
                ExerciseStep("World's Greatest Lunge Twist", "Step forward and rotate your upper back gently toward your front knee.", 120, "Opens hips and upper spine together."),
                ExerciseStep("Grounded Recovery Breath", "Stand or lie comfortably for slow, appreciative breaths.", 120, "Notice how capable your body feels.")
            )
        ),
        MovementRoutine(
            id = "full_20_strength",
            title = "Full Session — 20 Min Capable Body Routine",
            category = "FULL",
            durationMinutes = 20,
            difficulty = "All Levels",
            focusTags = "Strength • Core • Full Body",
            description = "For high-energy days when you have 20 minutes to build strength, balance, and stamina.",
            steps = listOf(
                ExerciseStep("Full Joint Warm-Up", "Wrists, shoulders, spine, hips, and ankles dynamic circles.", 180, "Warm up every joint smoothly."),
                ExerciseStep("Squat & Reverse Lunge Circuit", "Alternate steady squats and controlled step-back lunges.", 240, "Rest whenever needed."),
                ExerciseStep("Push-Up & Prone Y-T-W Posture Lifts", "Strengthen chest, arms, and upper-back posture muscles.", 240, "Builds effortless upright posture."),
                ExerciseStep("Dead Bug & Forearm Plank Stability", "Core stability flow to support your spine.", 240, "Steady breathing throughout."),
                ExerciseStep("Full-Body Evening Stretch & Cool-Down", "Seated forward fold, butterfly hip release, and child's pose.", 300, "Let your nervous system settle.")
            )
        ),
        MovementRoutine(
            id = "low_energy_gentle",
            title = "Low Energy — 7 Min Gentle Walk & Unwind",
            category = "LOW ENERGY",
            durationMinutes = 7,
            difficulty = "Gentle",
            focusTags = "Low Energy • Recovery • Calm",
            description = "When you feel tired after school or Drona—gentle movement that restores energy instead of draining it.",
            steps = listOf(
                ExerciseStep("Easy Room Walk & Arm Swings", "Walk slowly around your room or balcony while swinging arms loosely.", 120, "No pressure, just gentle circulation."),
                ExerciseStep("Seated Cat-Cow & Side Reach", "Sit comfortably and lengthen both sides of your waist.", 120, "Soften your shoulders."),
                ExerciseStep("Knee-to-Chest & Legs Up Rest", "Lie down and rest your legs up or hug knees gently.", 180, "Rest is productive too.")
            )
        )
    )

    fun defaultCalendarEvents(todayDateString: String): List<CalendarEventEntity> {
        val cal = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        fun plusDays(days: Int): String {
            val c = cal.clone() as java.util.Calendar
            c.add(java.util.Calendar.DAY_OF_YEAR, days)
            return sdf.format(c.time)
        }
        return listOf(
            CalendarEventEntity(
                title = "Physics Rotational Motion Test",
                dateString = plusDays(1),
                endDateString = plusDays(1),
                startTime = "10:30 AM",
                endTime = "11:30 AM",
                isAllDay = false,
                category = "EXAMS / TESTS",
                priority = "HIGH",
                location = "School Physics Hall",
                locationOrNotes = "Class 11 Unit Test • Focus on Moment of Inertia, Torque & Rolling without slipping",
                reminderPreference = "Day before & 1 hour before",
                linkedProjectOrGoal = "Strengthen Class 11 Physics & Maths fundamentals",
                attendanceOrCompletionStatus = "PREPARING",
                beforeEventChecklistCsv = "Review rotational formula sheet,Re-solve 3 logged mistakes,Pack geometry box & pens,Sleep on time by 10:30 PM",
                duringEventNotes = "Read numerical given values carefully; check sign convention first.",
                afterEventReflection = "",
                preparationStarted = true,
                preparationCompletedCount = 2
            ),
            CalendarEventEntity(
                title = "NCSC Regional Science Congress",
                dateString = plusDays(2), // e.g. 11 October
                endDateString = plusDays(4), // e.g. 13 October (Multi-day across 3 dates!)
                startTime = "9:00 AM",
                endTime = "4:30 PM",
                isAllDay = false,
                category = "NCSC",
                priority = "HIGH",
                location = "Regional Science Centre",
                locationOrNotes = "Multi-day event (11 Oct – 13 Oct) • Smart EcoDrain Shield oral presentation, logbook & physical model showcase",
                reminderPreference = "Day before & 1 hour before",
                linkedProjectOrGoal = "Smart EcoDrain Shield",
                attendanceOrCompletionStatus = "PREPARING",
                beforeEventChecklistCsv = "Finalize presentation slides & charts,Prepare 90-second opening & 5-minute full script,Check project logbook & experimental data tables,Test physical prototype & pack backup batteries/materials,Practice judge Q&A aloud,Confirm travel & reporting timing",
                duringEventNotes = "Capture judge questions, feedback from mentors, and photos of presentation setup.",
                afterEventReflection = "",
                preparationStarted = true,
                preparationCompletedCount = 2
            ),
            CalendarEventEntity(
                title = "Krow User Validation Sprint",
                dateString = plusDays(3),
                endDateString = plusDays(3),
                startTime = "4:30 PM",
                endTime = "5:15 PM",
                isAllDay = false,
                category = "PROJECTS",
                priority = "MEDIUM",
                location = "Local Stores / Student Group",
                locationOrNotes = "Collect 3 shopkeeper / student responses on Krow prototype workflow",
                reminderPreference = "On event day",
                linkedProjectOrGoal = "Krow",
                attendanceOrCompletionStatus = "PLANNED",
                beforeEventChecklistCsv = "Verify demo screens load offline,Prepare 3 short user questions,Carry notebook for observations",
                preparationStarted = true,
                preparationCompletedCount = 1
            ),
            CalendarEventEntity(
                title = "Sunday Family Time & Evening Outing",
                dateString = plusDays(5),
                endDateString = plusDays(5),
                startTime = "5:00 PM",
                endTime = "8:30 PM",
                isAllDay = false,
                category = "FAMILY TIME",
                priority = "HIGH",
                location = "Home / City Walk",
                locationOrNotes = "Finish key revision early so evening is 100% present and guilt-free with parents",
                reminderPreference = "On event day",
                linkedProjectOrGoal = "WELLNESS",
                attendanceOrCompletionStatus = "PLANNED",
                beforeEventChecklistCsv = "Complete 1 light revision block in the morning,Put phone on Do Not Disturb at 5:00 PM",
                preparationStarted = false,
                preparationCompletedCount = 0
            ),
            CalendarEventEntity(
                title = "Drona PCM Weekly Assessment",
                dateString = plusDays(7),
                endDateString = plusDays(7),
                startTime = "6:00 PM",
                endTime = "8:00 PM",
                isAllDay = false,
                category = "COACHING",
                priority = "MEDIUM",
                location = "Drona Coaching Centre",
                locationOrNotes = "Thermodynamics + Sequences & Series + Chemical Bonding",
                reminderPreference = "Day before & 1 hour before",
                linkedProjectOrGoal = "ACADEMICS",
                attendanceOrCompletionStatus = "PLANNED",
                beforeEventChecklistCsv = "Revise Thermodynamics work formulas,Practice 10 mixed PYQs,Pack Drona notebook",
                preparationStarted = false,
                preparationCompletedCount = 0
            ),
            CalendarEventEntity(
                title = "STEM Scholarship Portfolio Check",
                dateString = plusDays(21),
                endDateString = plusDays(21),
                startTime = "5:00 PM",
                endTime = "6:00 PM",
                isAllDay = false,
                category = "SCHOLARSHIPS",
                priority = "HIGH",
                location = "ASTHA OS Profile",
                locationOrNotes = "Verify Krow & Smart EcoDrain Shield evidence artifacts and essay draft",
                reminderPreference = "For prep milestones",
                linkedProjectOrGoal = "SCHOLARSHIP / APPLICATION READINESS",
                attendanceOrCompletionStatus = "PLANNED",
                beforeEventChecklistCsv = "Check eligibility & official portal dates,Gather verified project links,Outline 250-word impact statement",
                preparationStarted = true,
                preparationCompletedCount = 1
            ),
            CalendarEventEntity(
                title = "School Science & Innovation Lab Check",
                dateString = todayDateString,
                endDateString = todayDateString,
                startTime = "11:30 AM",
                endTime = "12:30 PM",
                isAllDay = false,
                category = "SCHOOL",
                priority = "NORMAL",
                location = "School Innovation Lab",
                locationOrNotes = "Peer walkthrough & mentor check-in for Smart EcoDrain Shield",
                reminderPreference = "At chosen time",
                linkedProjectOrGoal = "Smart EcoDrain Shield",
                attendanceOrCompletionStatus = "PREPARING",
                beforeEventChecklistCsv = "Carry logbook,Explain filtration stages clearly",
                preparationStarted = true,
                preparationCompletedCount = 1
            )
        )
    }

    fun defaultSpecialDayPlans(todayDateString: String): List<SpecialDayPlanEntity> {
        val cal = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        fun plusDays(days: Int): String {
            val c = cal.clone() as java.util.Calendar
            c.add(java.util.Calendar.DAY_OF_YEAR, days)
            return sdf.format(c.time)
        }
        return listOf(
            SpecialDayPlanEntity(
                dateString = todayDateString,
                dayType = "BALANCED DAY",
                customFocusNote = "1 focused study block + 1 NCSC prep block + calm evening reset",
                customStructureItemsCsv = "Focused Physics problem block (25m),NCSC 90-second presentation rehearsal (15m),10-min mobility reset & evening wind-down"
            ),
            SpecialDayPlanEntity(
                dateString = plusDays(1),
                dayType = "REVISION DAY",
                customFocusNote = "Physics Test day + Error Log spaced review",
                customStructureItemsCsv = "Morning formula warm-up (15m),School Physics Test,Evening Error Log review & light Chemistry recap"
            ),
            SpecialDayPlanEntity(
                dateString = plusDays(2),
                dayType = "COMPETITION PREPARATION DAY",
                customFocusNote = "NCSC Day 1 — Stay calm, confident, and hydrated",
                customStructureItemsCsv = "Check prototype & logbook before leaving,Present Smart EcoDrain Shield with clarity,Jot down judge questions & take a restful evening"
            ),
            SpecialDayPlanEntity(
                dateString = plusDays(5),
                dayType = "FAMILY TIME DAY",
                customFocusNote = "Finish key revision early so the evening is completely free with parents",
                customStructureItemsCsv = "1 early 25-min revision block,Zero heavy project tasks in the evening,Unplugged quality time with family & calm reflection"
            )
        )
    }

    fun defaultEventPrepMilestones(todayDateString: String): List<EventPrepMilestoneEntity> {
        val cal = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        fun plusDays(days: Int): String {
            val c = cal.clone() as java.util.Calendar
            c.add(java.util.Calendar.DAY_OF_YEAR, days)
            return sdf.format(c.time)
        }
        return listOf(
            // NCSC Preparation Milestones (Event ID 2)
            EventPrepMilestoneEntity(
                eventId = 2,
                eventTitle = "NCSC Regional Science Congress",
                milestoneKey = "ncsc_slides_finalize",
                phase = "BEFORE",
                stepTitle = "Finalize NCSC presentation charts & problem slide",
                stepInstruction = "Verify flow-rate data table and problem statement clarity.",
                targetDateString = todayDateString,
                durationMinutes = 20,
                status = "SUGGESTED"
            ),
            EventPrepMilestoneEntity(
                eventId = 2,
                eventTitle = "NCSC Regional Science Congress",
                milestoneKey = "ncsc_speaking_script",
                phase = "BEFORE",
                stepTitle = "Rehearse 90-second opening & judge Q&A aloud",
                stepInstruction = "Speak through problem, EcoDrain mechanism, and test results without reading notes.",
                targetDateString = plusDays(1),
                durationMinutes = 15,
                status = "SUGGESTED"
            ),
            EventPrepMilestoneEntity(
                eventId = 2,
                eventTitle = "NCSC Regional Science Congress",
                milestoneKey = "ncsc_pack_prototype",
                phase = "BEFORE",
                stepTitle = "Check logbook, prototype & backup materials",
                stepInstruction = "Pack physical model, printed charts, ID card, and confirm travel timing.",
                targetDateString = plusDays(1),
                durationMinutes = 15,
                status = "SUGGESTED"
            ),
            EventPrepMilestoneEntity(
                eventId = 2,
                eventTitle = "NCSC Regional Science Congress",
                milestoneKey = "ncsc_during_notes",
                phase = "DURING",
                stepTitle = "Capture judge questions & peer observations",
                stepInstruction = "Write down 2 specific pieces of feedback from judges during NCSC.",
                targetDateString = plusDays(2),
                durationMinutes = 10,
                status = "SUGGESTED"
            ),
            EventPrepMilestoneEntity(
                eventId = 2,
                eventTitle = "NCSC Regional Science Congress",
                milestoneKey = "ncsc_after_evidence",
                phase = "AFTER",
                stepTitle = "Save NCSC certificate & reflection to Application Profile",
                stepInstruction = "Log what went well and update Smart EcoDrain Shield evidence checklist.",
                targetDateString = plusDays(5),
                durationMinutes = 15,
                status = "SUGGESTED"
            ),
            // Physics Test Preparation Milestones (Event ID 1)
            EventPrepMilestoneEntity(
                eventId = 1,
                eventTitle = "Physics Rotational Motion Test",
                milestoneKey = "phys_test_formulas",
                phase = "BEFORE",
                stepTitle = "Revise Moment of Inertia & Rolling formulas + 3 logged mistakes",
                stepInstruction = "Active recall on blank paper; re-solve 2 rotational numericals.",
                targetDateString = todayDateString,
                durationMinutes = 25,
                status = "SUGGESTED"
            ),
            EventPrepMilestoneEntity(
                eventId = 1,
                eventTitle = "Physics Rotational Motion Test",
                milestoneKey = "phys_test_after_log",
                phase = "AFTER",
                stepTitle = "Log any tricky test questions into Error Log",
                stepInstruction = "Capture 1–2 mistakes while fresh so spaced review handles them automatically.",
                targetDateString = plusDays(2),
                durationMinutes = 15,
                status = "SUGGESTED"
            )
        )
    }

    data class DayTypeTemplate(
        val typeName: String,
        val tagline: String,
        val recommendedLoadLabel: String,
        val defaultStructure: List<String>
    )

    val dayTypeTemplates: List<DayTypeTemplate> = listOf(
        DayTypeTemplate(
            "STUDY DAY",
            "Deep conceptual clarity & problem practice",
            "2–3 focused study blocks + 1 error review + movement reset",
            listOf("1–2 focused PCM/CS problem blocks", "1 Error Log active recall review", "1 short movement & hydration reset")
        ),
        DayTypeTemplate(
            "REVISION DAY",
            "Consolidate formulas, weak topics & past mistakes",
            "Short high-recall sprints + zero new chapter overload",
            listOf("Formula & concept sheet active recall", "Re-solve 4 logged mistakes from Error Log", "Light evening wind-down")
        ),
        DayTypeTemplate(
            "PROJECT DAY",
            "Ship tangible progress on Krow or Smart EcoDrain Shield",
            "1 main build block + 1 testing/evidence step + light revision",
            listOf("1 deep build/prototype block (30m)", "1 testing, user feedback, or documentation step", "1 light 15-min academic revision")
        ),
        DayTypeTemplate(
            "RESEARCH / EXPLORATION DAY",
            "Follow curiosity, read deeply, and explore new ideas",
            "Curiosity-driven exploration without rigid pressure",
            listOf("Explore 1 research paper, experiment, or AI concept", "Capture 3 insights or questions in notes", "Gentle outdoor walk or stretch")
        ),
        DayTypeTemplate(
            "COMPETITION PREPARATION DAY",
            "Calm, confident readiness for NCSC, hackathons & fairs",
            "Presentation rehearsal + prototype check + protected rest",
            listOf("Rehearse 90-second & 5-minute presentation aloud", "Verify prototype, charts, and judge Q&A points", "Protect sleep & keep academic load light")
        ),
        DayTypeTemplate(
            "SCHOLARSHIP / APPLICATION DAY",
            "Organize real evidence, essays & verified opportunities",
            "1 portfolio artifact update + 1 essay/application step",
            listOf("Update 1 verified evidence item in Profile", "Draft or refine 1 application/scholarship response", "1 short study maintenance block")
        ),
        DayTypeTemplate(
            "SKILL-BUILDING DAY",
            "Hands-on Python, Kotlin, communication, or design practice",
            "Practice 1 concrete skill by building something small",
            listOf("30-min hands-on coding or communication exercise", "Apply skill to a small real problem", "Movement & posture reset")
        ),
        DayTypeTemplate(
            "REST / RESET DAY",
            "Intentional recovery so your mind and body feel fresh",
            "Maximum 1 optional light task • Zero guilt",
            listOf("Maximum 1 light optional task (or full rest)", "Hydration, gentle stretch, or sunlit walk", "Guilt-free downtime & early wind-down")
        ),
        DayTypeTemplate(
            "FAMILY TIME DAY",
            "Present, warm, uninterrupted time with parents & family",
            "Finish 1 small priority early so the evening stays 100% free",
            listOf("1 small essential task early in the day", "Protected unplugged family time", "Warm evening reflection")
        ),
        DayTypeTemplate(
            "FRIENDS / SOCIAL DAY",
            "Connection, laughter, and recharge with friends",
            "Light essentials early • Present social time",
            listOf("Complete 1 quick must-do early", "Enjoy time with friends without phone stress", "Calm evening wind-down")
        ),
        DayTypeTemplate(
            "OUTDOOR / EXPERIENCE DAY",
            "Step outside, observe the real world & refresh perspective",
            "Outdoor activity + light reflection",
            listOf("Outdoor walk, visit, or hands-on experience", "Hydrate and rest well", "Capture 1 memorable moment tonight")
        ),
        DayTypeTemplate(
            "BALANCED DAY",
            "Steady progress across study, building, and self-care",
            "1 academic block + 1 project/prep step + 1 wellness habit",
            listOf("1 focused study block (25m)", "1 project or event prep step (20m)", "1 movement/skincare/sleep habit")
        ),
        DayTypeTemplate(
            "CUSTOM DAY",
            "Design your own rhythm for today",
            "Tailored to what matters most to you today",
            listOf("Choose your #1 meaningful focus", "Protect 1 rest or self-care block", "Close the day peacefully")
        )
    )

    fun defaultMonthlyGoals(): List<MonthlyGoalEntity> = listOf(
        MonthlyGoalEntity(
            monthLabel = "October 2026",
            domain = "ACADEMICS",
            title = "Strengthen Class 11 Physics & Maths fundamentals",
            weeklyTarget = "Complete 3 focused 30-min problem sets + Error Log review",
            todaySmallestStep = "Solve 4 mixed questions without checking notes",
            linkedLongTermArea = "ACADEMICS",
            progressStepsDone = 3,
            progressStepsTotal = 5,
            reviewDecision = "MUST CONTINUE",
            isCompleted = false
        ),
        MonthlyGoalEntity(
            monthLabel = "October 2026",
            domain = "PROJECT",
            title = "Validate Krow with real user feedback",
            weeklyTarget = "Contact 3 potential users & log structured workflow observations",
            todaySmallestStep = "Get 1 piece of user feedback on Krow prototype",
            linkedLongTermArea = "PROJECTS",
            progressStepsDone = 2,
            progressStepsTotal = 4,
            reviewDecision = "MUST CONTINUE",
            isCompleted = false
        ),
        MonthlyGoalEntity(
            monthLabel = "October 2026",
            domain = "COMPETITION",
            title = "Prepare and present Smart EcoDrain Shield confidently for NCSC",
            weeklyTarget = "Practice 90-second pitch & complete filtration flow-test log",
            todaySmallestStep = "Practice 90-second problem & innovation explanation",
            linkedLongTermArea = "COMPETITIONS",
            progressStepsDone = 3,
            progressStepsTotal = 4,
            reviewDecision = "MUST CONTINUE",
            isCompleted = false
        ),
        MonthlyGoalEntity(
            monthLabel = "October 2026",
            domain = "PROFILE",
            title = "Document real evidence from my strongest project",
            weeklyTarget = "Add 1 verified artifact or testing note to Application Profile",
            todaySmallestStep = "Update Krow / EcoDrain evidence checklist item",
            linkedLongTermArea = "SCHOLARSHIP / APPLICATION READINESS",
            progressStepsDone = 2,
            progressStepsTotal = 3,
            reviewDecision = "SHOULD CONTINUE",
            isCompleted = false
        ),
        MonthlyGoalEntity(
            monthLabel = "October 2026",
            domain = "WELLNESS",
            title = "Maintain a consistent sleep + 10-min movement routine",
            weeklyTarget = "5 days of movement + 9:40 PM wind-down",
            todaySmallestStep = "10-minute mobility reset & evening wind-down",
            linkedLongTermArea = "WELLNESS",
            progressStepsDone = 4,
            progressStepsTotal = 5,
            reviewDecision = "MUST CONTINUE",
            isCompleted = false
        )
    )

    fun defaultLongTermGoals(): List<LongTermGoalEntity> = listOf(
        LongTermGoalEntity(
            area = "ACADEMICS",
            visionStatement = "Master Class 11 & 12 PCM + CS concepts deeply through active recall and independent problem solving.",
            class11Milestone = "Strong conceptual foundation in Mechanics, Thermodynamics, Calculus & Python",
            class12Milestone = "High board & competitive exam readiness with zero backlog",
            currentMonthlyFocus = "Strengthen Class 11 Physics & Maths fundamentals",
            weeklyTarget = "3 targeted problem sets + spaced Error Log review",
            todayAction = "Solve 4 mixed questions in current chapter"
        ),
        LongTermGoalEntity(
            area = "PROJECTS",
            visionStatement = "Build real-world technical products (Krow, AI Study Tools) with documented users and measurable utility.",
            class11Milestone = "Launch Krow MVP and validate with real local users",
            class12Milestone = "Open-source portfolio & documented real-world adoption",
            currentMonthlyFocus = "Strengthen Krow with real user feedback",
            weeklyTarget = "Contact 3 potential users",
            todayAction = "Send 1 feedback form / record 1 user observation"
        ),
        LongTermGoalEntity(
            area = "RESEARCH / INNOVATION",
            visionStatement = "Investigate practical environmental & AI engineering problems with empirical testing.",
            class11Milestone = "Smart EcoDrain Shield physical model & flow-rate experiments",
            class12Milestone = "Technical paper / research synopsis for national fairs",
            currentMonthlyFocus = "Document EcoDrain experimental methodology & test data",
            weeklyTarget = "Log 1 structured trial result",
            todayAction = "Record debris-separation test observation"
        ),
        LongTermGoalEntity(
            area = "COMPETITIONS",
            visionStatement = "Present original STEM & startup work confidently at NCSC, IRIS, and hackathons.",
            class11Milestone = "NCSC presentation & regional/national STEM showcase",
            class12Milestone = "National hackathon & science fair finals",
            currentMonthlyFocus = "Prepare Smart EcoDrain Shield NCSC presentation",
            weeklyTarget = "2 speaking rehearsals + Q&A prep",
            todayAction = "Practice 90-second innovation explanation"
        ),
        LongTermGoalEntity(
            area = "LEADERSHIP / IMPACT",
            visionStatement = "Create tangible usefulness for students and local community members.",
            class11Milestone = "Peer study tools & campus sustainability awareness",
            class12Milestone = "Documented community deployment & mentorship",
            currentMonthlyFocus = "Gather real user feedback on student/shopkeeper workflows",
            weeklyTarget = "1 community/user conversation",
            todayAction = "Ask 1 user what slows down their workflow"
        ),
        LongTermGoalEntity(
            area = "TECHNICAL SKILLS",
            visionStatement = "Fluency in Python, Kotlin/Compose, AI agent workflows, and hardware prototyping.",
            class11Milestone = "Build end-to-end Android & Python prototypes independently",
            class12Milestone = "Advanced AI/CS project architecture",
            currentMonthlyFocus = "Apply Python & Kotlin skills directly inside Krow and Study Tools",
            weeklyTarget = "1 clean feature or algorithm implementation",
            todayAction = "Refine 1 prototype screen or Python function"
        ),
        LongTermGoalEntity(
            area = "COMMUNICATION",
            visionStatement = "Explain complex technical and scientific ideas clearly, concisely, and persuasively.",
            class11Milestone = "Crisp 90-second project pitches & clear documentation",
            class12Milestone = "Compelling undergraduate essays & interview readiness",
            currentMonthlyFocus = "Master NCSC oral presentation & judge Q&A",
            weeklyTarget = "Practice speaking aloud twice a week",
            todayAction = "Speak through problem statement in 60 seconds"
        ),
        LongTermGoalEntity(
            area = "SCHOLARSHIP / APPLICATION READINESS",
            visionStatement = "Build an authentic, evidence-backed Class 11–12 portfolio for top undergraduate programs and scholarships.",
            class11Milestone = "Organize verified evidence across Projects, Research, Academics & Impact",
            class12Milestone = "Complete applications, essays, and verified recommendations ahead of deadlines",
            currentMonthlyFocus = "Document evidence from strongest projects (no fabricated claims)",
            weeklyTarget = "Verify 1 portfolio artifact",
            todayAction = "Update 1 missing evidence item in Application Profile"
        ),
        LongTermGoalEntity(
            area = "WELLNESS",
            visionStatement = "Stay energetic, strong, well-rested, and calm so ambition feels sustainable.",
            class11Milestone = "Consistent after-school recovery, 10-min movement & 9:40 PM wind-down",
            class12Milestone = "Calm, steady energy through exam & application season",
            currentMonthlyFocus = "Daily hydration, mobility reset, and sleep protection",
            weeklyTarget = "5 movement sessions + nightly brain dump",
            todayAction = "10-minute mobility reset + water break"
        )
    )

    fun defaultRecentWins(todayDateString: String): List<RecentWinEntity> = listOf(
        RecentWinEntity(
            title = "Physics questions",
            whatChangedDetail = "Physics: 5 rotational dynamics questions practiced",
            category = "STUDY",
            dateString = todayDateString,
            timestamp = System.currentTimeMillis() - 7200_000L
        ),
        RecentWinEntity(
            title = "Krow prototype update",
            whatChangedDetail = "Krow: Prototype flow documented & ready for user feedback",
            category = "BUILD",
            dateString = todayDateString,
            timestamp = System.currentTimeMillis() - 3600_000L
        ),
        RecentWinEntity(
            title = "Reviewed 2 mistakes",
            whatChangedDetail = "Error Log: Reviewed rolling friction & isothermal work sign conventions",
            category = "STUDY",
            dateString = todayDateString,
            timestamp = System.currentTimeMillis() - 1800_000L
        )
    )

    fun defaultSetbackRecoveries(todayDateString: String): List<SetbackRecoveryEntity> = listOf(
        SetbackRecoveryEntity(
            dateString = todayDateString,
            eventTitle = "Physics kinematics test score was lower than expected (14/25)",
            category = "ACADEMIC",
            whatActuallyHappened = "Lost 8 marks due to sign conventions in acceleration vectors and rushing through the last 2 multi-step numericals without checking units.",
            howAmIFeeling = "Disappointed and anxious that my coaching prep isn't showing immediate results.",
            whatIsStillTrueAboutMe = "I understand the underlying physical laws. I am capable of disciplined problem solving when I slow down and draw the free-body diagram.",
            whatWasOutOfMyControl = "Surprise time pressure and tricky phrasing of question 4.",
            whatWasInMyControl = "Skipping diagram setup, not logging sign convention errors earlier, and rushing in the last 10 minutes.",
            actionableLesson = "Always spend 30 seconds drawing coordinates and writing positive direction before computing vectors.",
            gentleNextStep = "Re-solve the 2 missed numericals untimed into the Error Log with clear step diagrams.",
            isResolved = true
        ),
        SetbackRecoveryEntity(
            dateString = todayDateString,
            eventTitle = "Junior AI Challenge idea rejected at regional screening",
            category = "COMPETITION",
            whatActuallyHappened = "The jury noted the problem statement was broad and lacked measured validation from real local users.",
            howAmIFeeling = "Stung, because I spent weekend evenings writing the prototype code.",
            whatIsStillTrueAboutMe = "I can build working software fast. A rejection on user validation doesn't invalidate my technical competence — it gives me the exact playbook to fix.",
            whatWasOutOfMyControl = "Jury's preference for hardware/community projects over pure app concepts.",
            whatWasInMyControl = "Talking to 3 real target users before submitting instead of presenting only self-assumed data.",
            actionableLesson = "Ground future competition entries with 1 concrete user observation video or quote.",
            gentleNextStep = "Interview 1 classmate or teacher tomorrow about their actual workflow for Krow/EcoDrain.",
            isResolved = false
        )
    )

    fun defaultFinancialExperiments(todayDateString: String): List<FinancialExperimentEntity> = listOf(
        FinancialExperimentEntity(
            title = "Class 9 Maths & Science Foundation Tutoring",
            skillUsed = "Teaching & Conceptual Clarity",
            experimentStage = "IN_PROGRESS",
            estimatedEarningPotential = "₹1,500 / month",
            actualEarned = 1500.0,
            clientOrPlatform = "Neighborhood Junior Student",
            whatLearnedAboutValue = "Parents pay for patience, reliability, and structured progress notes — not just solving the homework problems.",
            dateString = todayDateString
        ),
        FinancialExperimentEntity(
            title = "Local Small Business Landing Page & Google Maps Setup",
            skillUsed = "Full-stack Web & Clean Design",
            experimentStage = "DELIVERED",
            estimatedEarningPotential = "₹3,000",
            actualEarned = 2500.0,
            clientOrPlatform = "Local Stationery & Print Shop",
            whatLearnedAboutValue = "Business owners value showing up on time, testing on mobile, and explaining in plain non-technical language.",
            dateString = todayDateString
        ),
        FinancialExperimentEntity(
            title = "NCSC / STEM Science Innovation Prize Submission",
            skillUsed = "Research Synthesis & Prototyping",
            experimentStage = "IN_PROGRESS",
            estimatedEarningPotential = "₹5,000 - ₹10,000 grant",
            actualEarned = 0.0,
            clientOrPlatform = "National Children's Science Congress",
            whatLearnedAboutValue = "Institutional funding rewards rigorous documentation, verified test data, and community impact.",
            dateString = todayDateString
        )
    )

    val confidenceLabScenarios: List<ConfidenceLabScenario> = listOf(
        ConfidenceLabScenario(
            id = "class_speaking",
            title = "Speaking Up in Class / Coaching",
            category = "Class Participation",
            difficulty = "Gentle",
            contextDescription = "You have a doubt or an alternative solution in Physics or Math class, but you hesitate to raise your hand because you worry the question might sound basic or wrong.",
            commonMistakeOrTrap = "Assuming everyone else already understands, or over-rehearsing the question for 10 minutes until the teacher moves on to another topic.",
            coreMindsetShift = "Asking a sincere clarifying question helps at least 5 other students who are too shy to ask. Good teachers respect clarity over pretense.",
            recommendedScript = "\"Sir/Ma'am, in step 3 where we substituted the tension T, could you clarify whether we assumed the pulley has negligible inertia?\"",
            bodyLanguageCue = "Sit upright, raise hand deliberately, make eye contact, and speak at a steady, unhurried pace.",
            practicePrompt = "Practice saying this question out loud twice at normal volume before class starts.",
            reflectionQuestions = listOf(
                "Did I speak clearly without apologizing before asking?",
                "Did the answer help clarify the concept?",
                "How did my chest feel right after asking?"
            )
        ),
        ConfidenceLabScenario(
            id = "project_pitch",
            title = "Explaining Your Project Clearly (30s Hook + 60s Demo)",
            category = "Project Pitch",
            difficulty = "Moderate",
            contextDescription = "Someone (a judge, mentor, or visitor) asks: 'So what is this project?' You need to explain Krow or EcoDrain without getting lost in technical trivia.",
            commonMistakeOrTrap = "Starting with lines of code, tech stack, or database schemas before explaining what human problem it solves.",
            coreMindsetShift = "Hook with the pain point first, show the working prototype, then state the concrete evidence.",
            recommendedScript = "\"Hi! Most student builders abandon projects because they build in isolation. Krow is a lightweight tool that forces you to test with 1 real user every week. Here is the working mobile prototype, and we already tested it with 3 students.\"",
            bodyLanguageCue = "Open posture, smile, show the screen directly to their line of sight, and pause for their response.",
            practicePrompt = "Rehearse the 30-second hook without looking at notes. Keep it under 4 sentences.",
            reflectionQuestions = listOf(
                "Did I state the user problem in the very first sentence?",
                "Did I mention evidence/traction early?",
                "Did I leave space for the listener to ask a question?"
            )
        ),
        ConfidenceLabScenario(
            id = "handling_criticism",
            title = "Handling Criticism & Tough Feedback Gracefully",
            category = "Handling Feedback",
            difficulty = "Stretched",
            contextDescription = "A teacher, judge, or teammate critiques your project or exam solution sharply: 'This idea isn't novel' or 'Your derivation has flaws.'",
            commonMistakeOrTrap = "Becoming instantly defensive, arguing back aggressively, or collapsing into shame and wanting to abandon the project.",
            coreMindsetShift = "Separate the critique of the work from the worth of your identity. Feedback is free consulting data on how external eyes see your work.",
            recommendedScript = "\"Thank you for pointing that out. That is a very valid perspective. Which specific aspect of the mechanism do you feel is the most vulnerable?\"",
            bodyLanguageCue = "Take a slow breath, nod thoughtfully, avoid crossing arms, and write down their key phrase.",
            practicePrompt = "Roleplay hearing harsh feedback. Pause 3 full seconds before saying 'Thank you for pointing that out.'",
            reflectionQuestions = listOf(
                "Did I pause before reacting?",
                "Did I extract the usable data point without self-criticism?",
                "Did my response keep the dialogue constructive?"
            )
        ),
        ConfidenceLabScenario(
            id = "setting_boundaries",
            title = "Saying NO or Holding a Boundary Gracefully",
            category = "Setting Boundaries",
            difficulty = "Moderate",
            contextDescription = "A classmate asks you to complete their assignment portion at 9 PM, or a social plan threatens your scheduled sleep or exam revision block.",
            commonMistakeOrTrap = "Saying yes out of guilt and feeling resentful, or giving a 10-minute convoluted lie as an excuse.",
            coreMindsetShift = "A clear, kind 'no' protects your focus and shows self-respect. People respect predictable boundaries.",
            recommendedScript = "\"I won't be able to take that on tonight because I'm on a strict rest and revision cutoff at 9:30 PM. I can take a look at it tomorrow at lunch if that helps.\"",
            bodyLanguageCue = "Warm tone, direct eye contact, no nervous laughter, no over-apologizing.",
            practicePrompt = "Deliver the refusal in a single calm sentence without saying 'I'm so so sorry 10 times.'",
            reflectionQuestions = listOf(
                "Was my answer clear and definitive?",
                "Did I avoid making up fake excuses?",
                "Did I protect my evening peace?"
            )
        ),
        ConfidenceLabScenario(
            id = "networking_mentor",
            title = "Talking to a Mentor, Speaker, or Judge",
            category = "Networking",
            difficulty = "Moderate",
            contextDescription = "After a workshop, hackathon, or seminar, you want to approach the guest speaker or judge to ask for guidance.",
            commonMistakeOrTrap = "Standing around awkwardly waiting for everyone to leave, or asking a generic question like 'How do I succeed?'",
            coreMindsetShift = "Mentors love specific curiosity. Compliment one exact point from their talk, state what you are actively building, and ask one narrow question.",
            recommendedScript = "\"Hello Dr. Sharma, thank you for the talk on environmental sensors. I'm building an eco-drainage filtration prototype for NCSC. What is the most reliable low-cost turbidity sensor you've seen work in school labs?\"",
            bodyLanguageCue = "Firm handshake or respectful namaste, clear introduction, hold your notebook ready.",
            practicePrompt = "Practice introducing yourself and asking your specific question in under 40 seconds.",
            reflectionQuestions = listOf(
                "Did I mention a specific element of their expertise?",
                "Was my question actionable and focused?",
                "Did I express sincere gratitude for their time?"
            )
        )
    )

    val financialLessons: List<FinancialLesson> = listOf(
        FinancialLesson(
            id = "value_creation",
            title = "Value Creation vs Working Hours",
            subtitle = "Money is an echo of solved problems",
            tag = "FOUNDATION",
            coreConcept = "You don't get rewarded simply for the amount of time you sit at a desk. You get rewarded for the difficulty of the problem you solve and the reliability of your delivery.",
            teenTakeaway = "A 1-hour clean script that saves someone 10 hours of manual data entry is worth far more than 10 hours of disorganized effort. Focus on building rare, high-utility capabilities.",
            actionableExercise = "Identify 1 repetitive, annoying problem a local shop, teacher, or peer has, and brainstorm how software or clear notes could solve it."
        ),
        FinancialLesson(
            id = "high_leverage_skills",
            title = "High-Leverage Teen Skills (Skills-to-Income)",
            subtitle = "Capabilities that compound over the next 5 years",
            tag = "SKILL-BUILDING",
            coreConcept = "The most lucrative and durable skills in technology and knowledge work are: 1) Full-stack problem prototyping, 2) Technical writing & documentation, 3) Clear data analysis, 4) Video/graphic storytelling, and 5) System reliability.",
            teenTakeaway = "Even 6 months of disciplined coding in Kotlin, Python, or UI systems gives you the leverage to build prototypes, win hackathons, and earn independently as a student.",
            actionableExercise = "Pick one project (like Krow) and treat its GitHub README and demo video as professional-grade portfolio assets."
        ),
        FinancialLesson(
            id = "student_budgeting",
            title = "Student 50/30/20 Budgeting Rule",
            subtitle = "Managing pocket money, prizes, and first earnings",
            tag = "MANAGEMENT",
            coreConcept = "Adapted for students: 50% Essentials & Long-term Savings (tools, books, future college fund), 30% Learning & Experimentation (domain names, hosting, courses, lab materials), 20% Guilt-free Personal Joy & Family treats.",
            teenTakeaway = "Saving isn't about deprivation — it is about buying autonomy. Having even ₹5,000 in your own emergency/tool fund gives you calm confidence to pursue ambitious ideas.",
            actionableExercise = "Review any money you received or earned this month and allocate it into the 50/30/20 buckets."
        ),
        FinancialLesson(
            id = "earning_experiments",
            title = "Small Earning Experiments for Teen Builders",
            subtitle = "Testing the market without compromising academics",
            tag = "PRACTICE",
            coreConcept = "Start with low-friction, high-dignity experiments: 1) Tutoring younger students in subjects you excel at, 2) Academic and innovation competitions with grants, 3) Open-source bounties and technical documentation, 4) Building mini tools for real local businesses.",
            teenTakeaway = "Your primary job right now is Class 11 PCM excellence and deep building. Earning experiments are for learning how commerce works, not for burning out your study schedule.",
            actionableExercise = "List 2 small earning experiments you could run during a vacation or project day that take less than 4 hours a week."
        ),
        FinancialLesson(
            id = "scam_immunity",
            title = "Financial Foundations & Scam Immunity",
            subtitle = "Protecting your future self from hype traps",
            tag = "SAFETY",
            coreConcept = "Avoid get-rich-quick online schemes, crypto day-trading apps, MLM networks, and gambling disguised as games. Real wealth is created slowly through skills, equity, ownership, and compounding interest.",
            teenTakeaway = "If an opportunity promises high returns with zero skill or zero effort, you are the product or the mark. Guard your attention and savings fiercely.",
            actionableExercise = "When you turn 18, set up a zero-balance student bank account with 2-factor authentication and start an index mutual fund SIP with even ₹500/month."
        )
    )

    fun defaultAcademicTopics(todayDateString: String): List<AcademicTopicEntity> = listOf(
        AcademicTopicEntity(
            subject = "Chemistry Physical",
            chapterName = "Thermodynamics & Thermochemistry",
            topicName = "First Law, Enthalpy & Hess's Law Calculations",
            syllabusType = "EXAM_SYLLABUS",
            isConceptUnderstood = true,
            hasIndependentRecall = true,
            practiceLevel = "EXAM_LEVEL",
            mistakePatterns = "Sign conventions in isothermal expansion (w = -2.303 nRT log V2/V1) and unit conversion J vs kJ",
            lastRevisionDate = todayDateString,
            nextRevisionDate = todayDateString,
            requiresTeacherGuidance = false,
            teacherGuidanceNotes = "",
            isChemistryFullMarksFocus = true,
            definitionsAndReactionsNotes = "State function vs Path function, Extensive vs Intensive properties, Standard Enthalpy of Formation = 0 for reference states",
            calculationsAndNumericalsNotes = "Always convert temperature to Kelvin; check whether work is done BY the system or ON the system before adding to Delta U",
            timedPaperScore = "23/25 on numericals section",
            dateAdded = todayDateString
        ),
        AcademicTopicEntity(
            subject = "Chemistry Organic/Inorganic",
            chapterName = "Structure of Atom",
            topicName = "Quantum Numbers & Electronic Configurations (Cr, Cu exceptions)",
            syllabusType = "EXAM_SYLLABUS",
            isConceptUnderstood = true,
            hasIndependentRecall = true,
            practiceLevel = "SCHOOL_LEVEL",
            mistakePatterns = "Confusing azimuthal quantum number l values for d and f subshells; forgetting half-filled stability of 3d5 4s1",
            lastRevisionDate = todayDateString,
            nextRevisionDate = todayDateString,
            requiresTeacherGuidance = false,
            teacherGuidanceNotes = "",
            isChemistryFullMarksFocus = true,
            definitionsAndReactionsNotes = "Heisenberg Uncertainty Principle, Pauli's Exclusion Principle, Hund's Rule of Maximum Multiplicity, Aufbau Principle",
            calculationsAndNumericalsNotes = "de Broglie wavelength: lambda = h / (m * v); Rydberg formula for hydrogen spectrum transitions",
            timedPaperScore = "Pending timed paper 2",
            dateAdded = todayDateString
        ),
        AcademicTopicEntity(
            subject = "Physics",
            chapterName = "System of Particles & Rotational Motion",
            topicName = "Torque, Angular Momentum & Moment of Inertia of Standard Bodies",
            syllabusType = "CURRENT",
            isConceptUnderstood = true,
            hasIndependentRecall = false,
            practiceLevel = "SCHOOL_LEVEL",
            mistakePatterns = "Moment of inertia about diameter vs tangent; forgetting parallel axis theorem applicability to center of mass only",
            lastRevisionDate = todayDateString,
            nextRevisionDate = todayDateString,
            requiresTeacherGuidance = true,
            teacherGuidanceNotes = "Ask coaching faculty to clarify rolling without slipping on an inclined plane with friction",
            isChemistryFullMarksFocus = false,
            definitionsAndReactionsNotes = "",
            calculationsAndNumericalsNotes = "tau = I * alpha; L = I * omega; Parallel axis theorem: I = I_cm + M * d^2",
            timedPaperScore = "",
            dateAdded = todayDateString
        ),
        AcademicTopicEntity(
            subject = "Mathematics",
            chapterName = "Trigonometric Functions",
            topicName = "Multiple & Sub-multiple Angles, Conditional Identities",
            syllabusType = "CURRENT",
            isConceptUnderstood = true,
            hasIndependentRecall = true,
            practiceLevel = "EXAM_LEVEL",
            mistakePatterns = "Sign errors in quadrant transformations when angle is in 2nd/3rd quadrant",
            lastRevisionDate = todayDateString,
            nextRevisionDate = todayDateString,
            requiresTeacherGuidance = false,
            teacherGuidanceNotes = "",
            isChemistryFullMarksFocus = false,
            definitionsAndReactionsNotes = "",
            calculationsAndNumericalsNotes = "sin 2A = 2 sin A cos A; cos 2A = cos^2 A - sin^2 A = 2 cos^2 A - 1 = 1 - 2 sin^2 A",
            timedPaperScore = "18/20",
            dateAdded = todayDateString
        ),
        AcademicTopicEntity(
            subject = "Computer Science & AI",
            chapterName = "Python & Algorithmic Logic",
            topicName = "Recursion & Sorting Algorithms (Binary Search, Merge Sort)",
            syllabusType = "CURRENT",
            isConceptUnderstood = true,
            hasIndependentRecall = true,
            practiceLevel = "MASTERED",
            mistakePatterns = "Base condition omissions leading to RecursionError on edge cases (empty list or 1-element list)",
            lastRevisionDate = todayDateString,
            nextRevisionDate = todayDateString,
            requiresTeacherGuidance = false,
            teacherGuidanceNotes = "",
            isChemistryFullMarksFocus = false,
            definitionsAndReactionsNotes = "",
            calculationsAndNumericalsNotes = "Time complexity of Binary Search: O(log n); Merge Sort: O(n log n)",
            timedPaperScore = "25/25",
            dateAdded = todayDateString
        )
    )

    fun defaultScienceExhibitionScorecards(): List<ScienceExhibitionScorecardEntity> = listOf(
        ScienceExhibitionScorecardEntity(
            ideaTitle = "Smart EcoDrain Shield (Dual-Stage Street Drain Filter)",
            exhibitionTheme = "Environmental & Municipal Innovation (NCSC / State Level)",
            problemImportanceScore = 9,
            evidenceProblemExistsScore = 9,
            existingSolutionsComparison = "Existing municipal storm drains use fixed iron gratings that clog with plastic bags within 15 minutes of heavy rain, causing localized street flooding.",
            genuineDifferentiation = "Dual-stage angled baffle that separates floating plastics from sediment, plus a simple mechanical float flag visible to sanitation workers.",
            scientificCorrectnessScore = 9,
            budgetRequiredInr = 1200.0,
            isBudgetFeasible = true,
            estimatedTimeDays = 14,
            workingDemoPossibilityScore = 9,
            measurableResultsScore = 8,
            safetyAndEnvironmentScore = 10,
            easeOfExplainingToJudgesScore = 9,
            potentialWeaknessesAndQuestions = "Jury will ask: 'How does it handle silt build-up over 3 months?' Answer: Removable sediment cartridge designed for standard municipal rake tools.",
            unknownInformation = "Long-term UV degradation rate of recycled HDPE mesh under direct sunlight requires 6-month outdoor testing.",
            isSelectedForExecution = true,
            executionMilestone = 6, // Testing & observations stage
            stopOrPivotStatus = "PROCEED"
        ),
        ScienceExhibitionScorecardEntity(
            ideaTitle = "Krow — User Feedback & Prototyping Accountability App",
            exhibitionTheme = "Digital Solutions & Youth Entrepreneurship",
            problemImportanceScore = 8,
            evidenceProblemExistsScore = 8,
            existingSolutionsComparison = "General task apps (Trello, Notion) track todo items, but do not enforce real user interviews or validate whether an idea is actually wanted.",
            genuineDifferentiation = "Forces the student builder to log 1 real recorded user conversation every week before unlocking new feature code blocks.",
            scientificCorrectnessScore = 8,
            budgetRequiredInr = 0.0,
            isBudgetFeasible = true,
            estimatedTimeDays = 7,
            workingDemoPossibilityScore = 10,
            measurableResultsScore = 8,
            safetyAndEnvironmentScore = 10,
            easeOfExplainingToJudgesScore = 8,
            potentialWeaknessesAndQuestions = "Judges might ask: 'Is software innovation recognized in traditional science exhibitions that prefer physical hardware?'",
            unknownInformation = "Check official NCSC sub-category guidelines whether mobile-first systems are accepted under technology development.",
            isSelectedForExecution = false,
            executionMilestone = 4,
            stopOrPivotStatus = "PROCEED"
        ),
        ScienceExhibitionScorecardEntity(
            ideaTitle = "Low-Cost Photodiode Drain Turbidity & Water Quality Sensor",
            exhibitionTheme = "Sensors & Community Science",
            problemImportanceScore = 8,
            evidenceProblemExistsScore = 7,
            existingSolutionsComparison = "Commercial nephelometric turbidity meters cost ₹15,000+ and require laboratory cuvettes.",
            genuineDifferentiation = "Uses a 3D-printed enclosure, 850nm infrared LED, and phototransistor to measure relative scattering for under ₹400.",
            scientificCorrectnessScore = 7,
            budgetRequiredInr = 1800.0,
            isBudgetFeasible = true,
            estimatedTimeDays = 25,
            workingDemoPossibilityScore = 7,
            measurableResultsScore = 7,
            safetyAndEnvironmentScore = 9,
            easeOfExplainingToJudgesScore = 7,
            potentialWeaknessesAndQuestions = "Ambient light interference and sensor window staining by organic algae.",
            unknownInformation = "Needs lab calibration curve against Formazin turbidity standards at local college chemistry department.",
            isSelectedForExecution = false,
            executionMilestone = 2,
            stopOrPivotStatus = "PROCEED"
        )
    )

    fun defaultScholarshipStrategies(): List<ScholarshipStrategyEntity> = listOf(
        ScholarshipStrategyEntity(
            name = "MEXT Japanese Government Undergraduate Scholarship (Embassy Recommendation)",
            country = "Japan",
            degreeSubjectEligibility = "Undergraduate degree in Natural Sciences (Math, Physics, Chemistry, CS) or Social Sciences",
            nationalityRequirements = "Indian citizenship (applies to Embassy of Japan in India)",
            academicRequirements = "12 years of formal schooling completed or expected; strong Class 11 & 12 marks in Math, Physics/Chemistry and English",
            ageAndCompletionRequirements = "Generally aged 17 to 25 at time of departure",
            languageRequirements = "Willingness to learn Japanese (1-year intensive preparatory Japanese study in Tokyo/Osaka included in scholarship)",
            requiredExaminations = "Embassy written exams: English, Mathematics, Chemistry/Physics; followed by Embassy interview",
            fundingCoverage = "100% Tuition + Monthly Stipend (~117,000 to 120,000 JPY/mo) + Roundtrip Airfare",
            isFullyFunded = true,
            excludedCosts = "Passport fees, medical checkup fee, personal discretionary spending",
            applicationFeeVerified = "Zero application fee (Verified from official Embassy portal)",
            applicationOpening = "Expected April / May 2027 for 2028 Academic Entry",
            applicationDeadline = "Expected Late May / Early June 2027",
            requiredDocuments = "Application form, Certificate of Enrollment / Expected Graduation, Academic Transcripts (Class 10, 11, and predicted 12), Recommendation Letter from Principal/Teacher",
            recommendationLetters = "1 confidential letter from School Principal or Senior STEM teacher",
            selectionProcess = "Stage 1: Document screening & Written exams in Delhi/regional centers -> Stage 2: Embassy interview -> Stage 3: MEXT Tokyo final confirmation",
            officialSourceLink = "https://www.in.emb-japan.go.jp/itpr_en/00_000108.html",
            lastVerifiedDate = "Verified October 2026",
            eligibilityStatus = "PREPARING",
            nextAction = "Build Class 11 PCM excellence; start foundational Hiragana/Katakana and practice past MEXT Math A/B papers",
            uncertaintiesToResolve = "Confirm whether Cambridge / CBSE predicted scores format requires notarization or simple school seal",
            maxAcceptableFamilyCostInr = 0.0,
            isMextSpecific = true,
            applicationYear = "2027 (when application is submitted)",
            universityEntryYear = "2028 (when university study commences in Japan)",
            cycleOfficialStatusNotice = "OFFICIAL STATUS: The 2027 entry cycle embassy-recommendation application in India closed in May/June 2026. The next official cycle for 2028 entry opens April/May 2027. Do not confuse application year with entry year."
        ),
        ScholarshipStrategyEntity(
            name = "KAIST Undergraduate International Student Scholarship",
            country = "South Korea",
            degreeSubjectEligibility = "B.S. in Computer Science, Electrical Engineering, Mechanical, or Applied Mathematics",
            nationalityRequirements = "Non-Korean citizenship",
            academicRequirements = "Top 5% high school academic record; strong STEM profile and demonstrable project evidence",
            ageAndCompletionRequirements = "High school graduate or expected graduate by August of entry year",
            languageRequirements = "English proficiency (TOEFL iBT 83+ or IELTS 6.5+ or Duolingo English Test 105+; KAIST coursework is 100% in English)",
            requiredExaminations = "Standardized test scores (SAT / ACT or national board exams like CBSE 12th)",
            fundingCoverage = "Full tuition grant (8 semesters) + Monthly living allowance (~350,000 KRW/mo) + National Health Insurance",
            isFullyFunded = true,
            excludedCosts = "Dormitory fee (~150,000 KRW/mo covered partially by stipend), flight tickets",
            applicationFeeVerified = "$80 USD (can request waiver based on demonstrated financial need)",
            applicationOpening = "Early Cycle: September; Regular Cycle: November",
            applicationDeadline = "Early: October; Regular: January",
            requiredDocuments = "Online application, Transcripts, 1 Recommendation letter, High school profile, Honors/awards portfolio",
            recommendationLetters = "1 letter from Math or Science teacher",
            selectionProcess = "Holistic evaluation of academic achievement, potential, and personal statement; optional online interview",
            officialSourceLink = "https://admission.kaist.ac.kr/intl-undergraduate",
            lastVerifiedDate = "Verified October 2026",
            eligibilityStatus = "ELIGIBLE",
            nextAction = "Document Krow and EcoDrain GitHub prototypes as verifiable portfolio submissions",
            uncertaintiesToResolve = "Check whether CBSE 10th & 11th marks sufficient for Early Cycle before 12th board results are out",
            maxAcceptableFamilyCostInr = 25000.0,
            isMextSpecific = false,
            applicationYear = "2027",
            universityEntryYear = "2028",
            cycleOfficialStatusNotice = "Classes conducted entirely in English. World top 40 university for engineering."
        ),
        ScholarshipStrategyEntity(
            name = "NUS ASEAN Undergraduate Scholarship",
            country = "Singapore",
            degreeSubjectEligibility = "Undergraduate degree in Computing, Engineering, Science, or Humanities at National University of Singapore",
            nationalityRequirements = "Citizens of ASEAN or regional designated countries",
            academicRequirements = "Exceptional Class 12 board marks (95%+ in PCM) or top JEE/Olympiad rank",
            ageAndCompletionRequirements = "Completed high school within 1 year of matriculation",
            languageRequirements = "English medium instruction certified by school",
            requiredExaminations = "Class 12 Board examinations",
            fundingCoverage = "Full subsidized tuition fees + S$5,800 annual living allowance + S$3,000 accommodation allowance + S$1,750 one-off computer allowance",
            isFullyFunded = true,
            excludedCosts = "Remaining personal expenses if living expenses exceed allowance",
            applicationFeeVerified = "S$20 SGD",
            applicationOpening = "October (for next academic year)",
            applicationDeadline = "Mid-January",
            requiredDocuments = "Academic transcripts, Co-curricular activities record, Personal essay",
            recommendationLetters = "School appraisal",
            selectionProcess = "Shortlisted candidates invited for interview in India (Delhi/Mumbai/Bangalore) or online",
            officialSourceLink = "https://nus.edu.sg/oam/scholarships/undergraduate-scholarships/asean-undergraduate-scholarship",
            lastVerifiedDate = "Verified October 2026",
            eligibilityStatus = "NEEDS_VERIFICATION",
            nextAction = "Verify whether Indian national applicants qualify under ASEAN regional quota or general international financial aid",
            uncertaintiesToResolve = "Verify country-specific eligibility criteria on the NUS admissions portal",
            maxAcceptableFamilyCostInr = 50000.0,
            isMextSpecific = false,
            applicationYear = "2027",
            universityEntryYear = "2028",
            cycleOfficialStatusNotice = "Bond requirement: 3 years working in Singapore-registered company after graduation."
        )
    )

    fun defaultScenarioPlans(): List<ScenarioPlanEntity> = listOf(
        ScenarioPlanEntity(
            goalCategory = "EXAM",
            title = "Class 11 Chemistry Term Exam (Principal's Full-Marks Challenge)",
            bestCaseOutcome = "100% full marks. Clear, flawless derivations, correct thermodynamic sign conventions, and precise step-by-step numericals.",
            bestCaseEvidenceToSupport = "3 consecutive error-free 45-minute timed papers, zero unlogged mistakes in the Error Log.",
            bestCaseActionToMaximize = "Write out definitions, reactions, and calculation formulas by hand every morning for 15 minutes. Double-check all unit conversions.",
            expectedCaseOutcome = "90%–95%. Thorough grasp of concepts with 1 minor slip in a multi-step calculation.",
            expectedCaseRealisticPlan = "Focus daily practice on Thermodynamics, Structure of Atom, and Chemical Bonding. Review school test past papers.",
            setbackCaseRisk = "Tricky numerical or harsh step-marking drops score to 75%–80%.",
            setbackPreventiveAction = "Solve 10 varied NCERT Exemplar problems untimed first to build conceptual immunity against surprise questions.",
            setbackRecoveryAlternative = "Treat the test as diagnostic feedback: log every lost mark into the Error Log, review with the teacher calmly, and rebuild without self-blame.",
            keyAssumptions = "Syllabus strictly aligns with CBSE Class 11 NCERT textbook chapters 1–6.",
            earlyWarningSigns = "Hesitating on which formula to use or taking more than 4 minutes on a single 3-mark problem.",
            nextDecisionPoint = "Mock Test 1 self-review date: 3 days before exam"
        ),
        ScenarioPlanEntity(
            goalCategory = "SCIENCE_EXHIBITION",
            title = "Smart EcoDrain Shield (NCSC / State Exhibition)",
            bestCaseOutcome = "Selected for State Level with National qualifier recommendation. Clean working demonstration with measured water flow data.",
            bestCaseEvidenceToSupport = "Working scale prototype tested with 10L simulated dirty water, video demonstration, and laminated scientific observation charts.",
            bestCaseActionToMaximize = "Conduct 3 timed rehearsals of the 90-second judge explanation hook. Keep a physical sample of trapped debris.",
            expectedCaseOutcome = "District recognition award and positive feedback on practical civic utility.",
            expectedCaseRealisticPlan = "Demonstrate the dual-stage filter mechanism reliably without water leakage in the presentation booth.",
            setbackCaseRisk = "Jury asks an unexpected technical question on UV degradation or challenges novelty.",
            setbackPreventiveAction = "Prepare the 'Known Limitations & Future Improvements' card honestly; judges respect humility and measured data over inflated claims.",
            setbackRecoveryAlternative = "Document the jury feedback in Krow/EcoDrain project log, extract the engineering improvement, and prepare for next regional hackathon.",
            keyAssumptions = "Table space and water pouring permission granted at exhibition venue.",
            earlyWarningSigns = "Prototype leaks during dry test run or explanation exceeds 2 minutes.",
            nextDecisionPoint = "Pre-exhibition dry run with science teacher: 1 week before event"
        ),
        ScenarioPlanEntity(
            goalCategory = "SCHOLARSHIP",
            title = "MEXT Japanese Government Undergraduate Scholarship (2028 Entry)",
            bestCaseOutcome = "Embassy recommendation nomination in the 2027 selection cycle, leading to full-ride undergraduate studies in Japan.",
            bestCaseEvidenceToSupport = "Consistently high scores in past MEXT Math A & English papers, strong Class 11 & 12 board marks, and verified prototype portfolio.",
            bestCaseActionToMaximize = "Solve 1 past paper question every weekend; build authentic extracurricular evidence around Krow and EcoDrain.",
            expectedCaseOutcome = "Competitive application reaching the written examination and interview stage.",
            expectedCaseRealisticPlan = "Maintain 90%+ in school PCM, write clear personal essays, and learn basic Japanese communication.",
            setbackCaseRisk = "Not selected in the highly competitive Indian applicant pool (only a small number of undergraduate awards nationwide).",
            setbackPreventiveAction = "Apply to parallel funded alternatives (KAIST, NUS ASEAN, domestic premier STEM programs like IISc/IIT/BITS).",
            setbackRecoveryAlternative = "A rejection from MEXT does not diminish academic worth. The rigorous preparation directly boosts Class 12 board and competitive entrance scores.",
            keyAssumptions = "Official MEXT guidelines for the next cycle will be published by Embassy of Japan in India in April 2027.",
            earlyWarningSigns = "Struggling with MEXT Math past papers or falling behind on school physics numericals.",
            nextDecisionPoint = "Official Embassy notification release date check: April 15, 2027"
        )
    )

    fun defaultProfileEvidenceLedger(todayDateString: String): List<ProfileEvidenceLedgerEntity> = listOf(
        ProfileEvidenceLedgerEntity(
            title = "Krow Mobile Prototype v0.3 Repository",
            category = "GITHUB",
            status = "COMPLETED",
            dateString = todayDateString,
            evidenceLinkOrFile = "https://github.com/astha/krow-os",
            whatWasLearnedAndDemonstrated = "Implemented full Kotlin Compose UI with Room persistence; demonstrated component modularity and reactive StateFlow architectures.",
            isExternallyVerified = true
        ),
        ProfileEvidenceLedgerEntity(
            title = "Smart EcoDrain Scale Filtration Test Log",
            category = "EXPERIMENT",
            status = "IN_PROGRESS",
            dateString = todayDateString,
            evidenceLinkOrFile = "Laboratory observation sheet #02 — 10L particulate test",
            whatWasLearnedAndDemonstrated = "Measured 82% retention of suspended plastic debris (>2mm) under simulated rainfall flow rates of 1.2 L/min.",
            isExternallyVerified = false
        ),
        ProfileEvidenceLedgerEntity(
            title = "Class 11 Physics & Chemistry Verified Error Log",
            category = "ACADEMIC",
            status = "COMPLETED",
            dateString = todayDateString,
            evidenceLinkOrFile = "Internal ASTHA OS Error Log (42 resolved problem corrections)",
            whatWasLearnedAndDemonstrated = "Demonstrates systematic metacognitive discipline, mistake categorization, and spaced retrieval practice.",
            isExternallyVerified = true
        ),
        ProfileEvidenceLedgerEntity(
            title = "Krow User Walkthrough Observations (3 Peer Interviews)",
            category = "USER_FEEDBACK",
            status = "COMPLETED",
            dateString = todayDateString,
            evidenceLinkOrFile = "Interview transcript notes (Student builders A, B, C)",
            whatWasLearnedAndDemonstrated = "Discovered that teen builders abandon projects because of lack of external accountability, validating Krow's core hypothesis.",
            isExternallyVerified = true
        )
    )

    fun defaultAngerOverwhelmPauses(todayDateString: String): List<AngerOverwhelmPauseEntity> = listOf(
        AngerOverwhelmPauseEntity(
            triggerEvent = "Principal challenged our class batch to aim for 100% in Chemistry during morning assembly, making me feel anxious and pressured.",
            firstImpulseOrEmotion = "Stung, annoyed, and feeling like my current hard work isn't recognized.",
            factualRealityCheck = "The principal is setting high school standards for upcoming board exams. It is not an attack on my character, intelligence, or worth.",
            alternativeInterpretation1 = "The challenge can be turned into a structured, game-like preparation system where I conquer every formula and reaction methodically.",
            alternativeInterpretation2 = "High external expectations simply confirm that people believe our class is capable of excellence.",
            chosenConstructiveResponse = "Breathed slowly for 2 minutes. Created the 'Chemistry Full-Marks Preparation Mission' with daily 20-minute written equation practice and error logs.",
            dateString = todayDateString,
            timestamp = System.currentTimeMillis() - 7200_000L
        )
    )
}



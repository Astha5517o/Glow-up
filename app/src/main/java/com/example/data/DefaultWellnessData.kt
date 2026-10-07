package com.example.data

object DefaultWellnessData {

    val supportiveQuotes = listOf(
        "Small steps count.",
        "Consistency beats perfection.",
        "Take care of yourself today.",
        "You're allowed to rest.",
        "One missed day doesn't erase your progress.",
        "Your body deserves care, not criticism.",
        "Progress can look different every day.",
        "You don't need to change your body to deserve confidence."
    )

    val focusOptions = listOf(
        "Move more",
        "Build strength",
        "Improve posture",
        "Stay hydrated",
        "Build a skincare routine",
        "Sleep better",
        "Feel more confident",
        "Create a consistent routine"
    )

    val freeTimeOptions = listOf(
        "Morning",
        "Afternoon",
        "Evening",
        "Flexible"
    )

    val durationOptions = listOf(5, 10, 15, 20)

    val nextWeekFocusOptions = listOf(
        "Move more",
        "Sleep earlier",
        "Drink water regularly",
        "Stay consistent with skincare",
        "Take more breaks",
        "Be kinder to myself"
    )

    fun defaultHabits(moveDuration: Int = 10, windDownTime: String = "9:30 PM"): List<DailyHabitEntity> = listOf(
        DailyHabitEntity(
            category = "HYDRATE",
            title = "Hydrate",
            subtitle = "Next reminder in 42 min",
            emoji = "💧",
            completedToday = false,
            sortOrder = 1
        ),
        DailyHabitEntity(
            category = "MOVE",
            title = "Move",
            subtitle = "$moveDuration min mobility",
            emoji = "🧘",
            completedToday = false,
            sortOrder = 2
        ),
        DailyHabitEntity(
            category = "GLOW",
            title = "Glow",
            subtitle = "Morning & evening care",
            emoji = "✨",
            completedToday = false,
            sortOrder = 3
        ),
        DailyHabitEntity(
            category = "SLEEP",
            title = "Sleep",
            subtitle = "Wind-down at $windDownTime",
            emoji = "😴",
            completedToday = false,
            sortOrder = 4
        ),
        DailyHabitEntity(
            category = "CONFIDENCE",
            title = "Confidence",
            subtitle = "Evening check-in",
            emoji = "🌸",
            completedToday = false,
            sortOrder = 5
        )
    )

    fun defaultSkincareSteps(): List<SkincareStepEntity> = listOf(
        // Morning
        SkincareStepEntity(period = "MORNING", title = "Wash face gently", completedToday = false, sortOrder = 1),
        SkincareStepEntity(period = "MORNING", title = "Moisturizer", completedToday = false, sortOrder = 2),
        SkincareStepEntity(period = "MORNING", title = "Sunscreen", completedToday = false, sortOrder = 3),
        SkincareStepEntity(period = "MORNING", title = "Brush teeth", completedToday = false, sortOrder = 4),
        SkincareStepEntity(period = "MORNING", title = "Comb/style hair", completedToday = false, sortOrder = 5),
        SkincareStepEntity(period = "MORNING", title = "Get ready", completedToday = false, sortOrder = 6),
        // Evening
        SkincareStepEntity(period = "EVENING", title = "Cleanse gently", completedToday = false, sortOrder = 1),
        SkincareStepEntity(period = "EVENING", title = "Moisturizer", completedToday = false, sortOrder = 2),
        SkincareStepEntity(period = "EVENING", title = "Brush teeth", completedToday = false, sortOrder = 3),
        SkincareStepEntity(period = "EVENING", title = "Prepare clothes/bag", completedToday = false, sortOrder = 4),
        SkincareStepEntity(period = "EVENING", title = "Wind down", completedToday = false, sortOrder = 5)
    )

    fun defaultHydrationReminders(): List<HydrationReminderEntity> = listOf(
        HydrationReminderEntity(timeLabel = "8:00 AM", hour24 = 8, minute = 0, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "10:00 AM", hour24 = 10, minute = 0, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "12:00 PM", hour24 = 12, minute = 0, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "2:00 PM", hour24 = 14, minute = 0, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "4:00 PM", hour24 = 16, minute = 0, isEnabled = true, completedToday = false),
        HydrationReminderEntity(timeLabel = "6:00 PM", hour24 = 18, minute = 0, isEnabled = true, completedToday = false)
    )

    val movementRoutines: List<MovementRoutine> = listOf(
        // MORNING
        MovementRoutine(
            id = "morning_5_wakeup",
            title = "5 Min Wake-Up Mobility",
            category = "MORNING",
            durationMinutes = 5,
            difficulty = "Beginner",
            focusTags = "Mobility • Energy",
            description = "Shake off sleepiness with gentle joint circles and full-body stretches.",
            steps = listOf(
                ExerciseStep("Deep Morning Reach", "Stand tall, interlace fingers overhead, and gently stretch upward while breathing deeply.", 60, "Inhale as you reach, exhale as your shoulders soften."),
                ExerciseStep("Gentle Neck & Shoulder Rolls", "Slowly roll your shoulders backward in wide circles, letting tension melt away.", 60, "Keep your jaw relaxed and breathe steadily."),
                ExerciseStep("Cat-Cow Spinal Flow", "Arch your back gently on the inhale, and round your spine comfortably on the exhale.", 60, "Move at the pace of your own breath."),
                ExerciseStep("Side Body Stretch", "Reach your right arm overhead and lean gently to the left, then switch sides.", 60, "Feel space opening along your ribs."),
                ExerciseStep("Ankle & Wrist Warm-up", "Circle your wrists and ankles slowly in both directions to wake up your joints.", 60, "Stand grounded and ready for your day.")
            )
        ),
        MovementRoutine(
            id = "morning_10_fullbody",
            title = "Full Body Reset",
            category = "MORNING",
            durationMinutes = 10,
            difficulty = "Beginner",
            focusTags = "Mobility • Posture",
            description = "A calm, energizing full-body flow to help you feel loose, upright, and comfortable.",
            steps = listOf(
                ExerciseStep("Sunrise Arm Sweeps", "Sweep both arms wide overhead as you inhale, and float them down as you exhale.", 90, "Slow, rhythmic breathing."),
                ExerciseStep("Thoracic Openers", "Hands behind your head, gently open your elbows wide and lift your chest slightly.", 90, "Notice your posture lengthening without stiffness."),
                ExerciseStep("Hip Circles & Weight Shifts", "Hands on hips, make slow comfortable circles to lubricate your hip joints.", 90, "Keep knees softly bent."),
                ExerciseStep("World's Greatest Lunge Stretch", "Step one foot forward into a gentle lunge and rotate your upper body toward your front leg.", 120, "Switch sides halfway through."),
                ExerciseStep("Hamstring & Calf Sweep", "Extend one heel forward with toes up and hinge slightly at the hips.", 90, "Gentle lengthen, never force the stretch."),
                ExerciseStep("Grounded Mountain Breath", "Stand tall with feet hip-width apart, palms facing forward, feeling strong and steady.", 120, "Three slow breaths to close your practice.")
            )
        ),
        // POSTURE
        MovementRoutine(
            id = "posture_5_routine",
            title = "5 Min Posture Routine",
            category = "POSTURE",
            durationMinutes = 5,
            difficulty = "Beginner",
            focusTags = "Posture • Upper Back",
            description = "Counteract slouching and backpack tension with gentle chest and upper-back openers.",
            steps = listOf(
                ExerciseStep("Chin Tucks", "Gently glide your chin straight back to align your ears over your shoulders.", 60, "Hold for 3 seconds, release gently."),
                ExerciseStep("Wall or Air Angels", "Bend elbows at 90 degrees and slowly slide arms up and down as if against a wall.", 75, "Keep your ribs relaxed."),
                ExerciseStep("Chest Doorway Opener", "Clasp hands behind your back or rest forearms on a doorframe to open your collarbones.", 75, "Breathe into the front of your chest."),
                ExerciseStep("Seated Spinal Twist", "Sit tall and gently rotate your torso to look over your left shoulder, then right.", 90, "Grow taller on every inhale.")
            )
        ),
        MovementRoutine(
            id = "posture_neck_shoulder",
            title = "Neck and Shoulder Mobility",
            category = "POSTURE",
            durationMinutes = 7,
            difficulty = "Gentle",
            focusTags = "Neck • Shoulders",
            description = "Release stiffness from studying, reading, or looking at your phone.",
            steps = listOf(
                ExerciseStep("Ear-to-Shoulder Release", "Drop your right ear toward your right shoulder. Let your left shoulder stay heavy.", 90, "Switch sides halfway through."),
                ExerciseStep("Shoulder Blade Squeezes", "Draw your shoulder blades gently together and down, then release completely.", 90, "Notice the difference between tension and ease."),
                ExerciseStep("Eagle Arm Hug", "Wrap your arms across your chest in a warm self-hug and breathe into your upper back.", 120, "Switch which arm is on top halfway."),
                ExerciseStep("Slow Half-Moon Neck Glides", "Trace a gentle half-circle with your chin from one collarbone to the other.", 120, "Move slowly and smoothly.")
            )
        ),
        MovementRoutine(
            id = "posture_desk_reset",
            title = "Desk Reset",
            category = "POSTURE",
            durationMinutes = 5,
            difficulty = "All Levels",
            focusTags = "Study Break • Focus",
            description = "A quick standing break between study sessions to refresh your eyes and posture.",
            steps = listOf(
                ExerciseStep("20-20-20 Eye Relaxation", "Look away from your screen at something 20 feet away and blink softly.", 60, "Let your eyes and forehead relax."),
                ExerciseStep("Standing Back Extension", "Stand up, rest hands on your lower hips, and gently lift your chest toward the ceiling.", 60, "Counteract sitting with ease."),
                ExerciseStep("Wrist & Finger Stretch", "Extend one arm forward and gently draw your fingers back with the other hand.", 60, "Great after typing or writing notes."),
                ExerciseStep("March & Shake Out", "March lightly in place and shake out your hands, arms, and legs.", 120, "Bring fresh energy back to your body.")
            )
        ),
        // STRENGTH
        MovementRoutine(
            id = "strength_beginner_bodyweight",
            title = "Beginner Bodyweight Routine",
            category = "STRENGTH",
            durationMinutes = 12,
            difficulty = "Beginner",
            focusTags = "Strength • Capability",
            description = "Build functional everyday strength at your own pace. Focus on how capable your body feels.",
            steps = listOf(
                ExerciseStep("Warm-Up Squats", "Feet shoulder-width apart, sit back comfortably as if lowering into a chair, then stand tall.", 120, "Exhale as you press through your heels."),
                ExerciseStep("Incline or Knee Push-Ups", "Place hands on a sturdy desk, wall, or floor on knees. Lower chest with control and press back.", 120, "Keep your core steady."),
                ExerciseStep("Glute Bridges", "Lie on your back with knees bent, feet flat. Lift your hips until knees, hips, and shoulders align.", 120, "Pause for a second at the top."),
                ExerciseStep("Bird-Dog Balance", "On hands and knees, extend opposite arm and leg straight out, then switch smoothly.", 120, "Focus on balance and stability."),
                ExerciseStep("Wall Sit Hold", "Lean your back against a wall with knees comfortably bent. Hold and breathe.", 120, "Take breaks whenever you need."),
                ExerciseStep("Cool-Down Child's Pose", "Sit hips back toward your heels and rest your forehead down.", 120, "Appreciate what your body did today.")
            )
        ),
        MovementRoutine(
            id = "strength_lower_body",
            title = "Lower-Body Strength",
            category = "STRENGTH",
            durationMinutes = 10,
            difficulty = "Beginner",
            focusTags = "Legs • Balance",
            description = "Feel grounded and strong in your legs and hips with controlled bodyweight movements.",
            steps = listOf(
                ExerciseStep("Bodyweight Squats", "Lower at a comfortable depth keeping your chest proud and knees tracking over toes.", 120, "Steady, unhurried pace."),
                ExerciseStep("Reverse Lunges", "Step one foot backward, lower gently, and step back to standing. Alternate legs.", 150, "Use a chair or wall for balance if helpful."),
                ExerciseStep("Standing Calf Raises", "Rise up onto the balls of your feet slowly, pause, and lower with control.", 120, "Feel the strength in your ankles and calves."),
                ExerciseStep("Side Leg Lifts", "Stand tall and lift one leg out to the side with control to strengthen outer hips.", 120, "Switch sides halfway."),
                ExerciseStep("Figure-Four Hip Stretch", "Cross one ankle over opposite knee and sit back slightly to stretch your hips.", 90, "Breathe deeply as you cool down.")
            )
        ),
        MovementRoutine(
            id = "strength_upper_body",
            title = "Upper-Body Strength",
            category = "STRENGTH",
            durationMinutes = 10,
            difficulty = "Beginner",
            focusTags = "Arms • Posture",
            description = "Strengthen your shoulders, back, and arms so carrying books and moving through life feels easier.",
            steps = listOf(
                ExerciseStep("Arm Circles & Plank Walkout", "Circle arms warm, then hinge down to a comfortable plank or wall plank.", 120, "Keep shoulders away from ears."),
                ExerciseStep("Wall or Knee Push-Ups", "Controlled push-ups focusing on smooth form rather than speed.", 150, "Rest whenever you want."),
                ExerciseStep("Prone Y-T-W Lifts", "Lie face down or hinge forward, lifting arms in Y, T, and W shapes to strengthen upper back.", 150, "Awesome for supporting natural posture."),
                ExerciseStep("Shoulder Tap Plank", "In a knee plank or incline plank, gently tap opposite shoulder with each hand.", 90, "Keep hips steady."),
                ExerciseStep("Cross-Body Shoulder Stretch", "Draw one arm across your chest and breathe slowly.", 90, "Switch sides halfway.")
            )
        ),
        MovementRoutine(
            id = "strength_core_stability",
            title = "Core Stability",
            category = "STRENGTH",
            durationMinutes = 8,
            difficulty = "Beginner",
            focusTags = "Core • Balance",
            description = "Support your spine and everyday balance with gentle, steady core stability exercises.",
            steps = listOf(
                ExerciseStep("Dead Bug Flow", "Lie on your back, knees in tabletop. Slowly lower opposite arm and heel toward the floor.", 120, "Keep your lower back comfortable on the mat."),
                ExerciseStep("Forearm or Knee Plank", "Hold a steady plank on knees or toes, breathing smoothly.", 90, "You are stronger than you think."),
                ExerciseStep("Side Plank Reach", "Rest on one forearm with knees bent, lifting your hips gently.", 120, "Switch sides halfway through."),
                ExerciseStep("Supine Knee Drops", "Lie on your back and gently rock bent knees side to side to release your core.", 150, "Slow, calming breaths.")
            )
        ),
        // RELAX
        MovementRoutine(
            id = "relax_evening_stretch",
            title = "Evening Stretching",
            category = "RELAX",
            durationMinutes = 10,
            difficulty = "Gentle",
            focusTags = "Relax • Unwind",
            description = "Let go of the school day and prepare your body for restful sleep.",
            steps = listOf(
                ExerciseStep("Seated Forward Fold", "Sit comfortably with legs extended or softly bent, reaching gently toward your shins.", 120, "Let your head and neck relax."),
                ExerciseStep("Butterfly Hip Release", "Bring the soles of your feet together and let your knees soften outward.", 120, "No pushing—just gravity and breath."),
                ExerciseStep("Child's Pose", "Kneel and fold forward, resting your arms alongside your body or out in front.", 120, "Every exhale lets go of today's stress."),
                ExerciseStep("Legs Up or Supine Rest", "Lie on your back and rest your calves on a pillow, bed, or wall.", 120, "Let your nervous system slow down."),
                ExerciseStep("Full Body Savasana Breath", "Lie comfortably and scan from your toes to your forehead, softening every muscle.", 120, "You did enough today.")
            )
        ),
        MovementRoutine(
            id = "relax_breathing",
            title = "Calming Breathwork",
            category = "RELAX",
            durationMinutes = 5,
            difficulty = "All Levels",
            focusTags = "Calm • Mindful",
            description = "Slow box breathing and extended exhales to quiet a busy mind.",
            steps = listOf(
                ExerciseStep("Settle & Ground", "Sit or lie down comfortably. Place one hand on your chest and one on your belly.", 60, "Notice the natural rise and fall of your breath."),
                ExerciseStep("4-4-4-4 Box Breathing", "Inhale for 4 counts, hold gently for 4, exhale for 4, rest for 4.", 120, "Gentle and unforced."),
                ExerciseStep("4-6 Calming Exhale", "Inhale softly through your nose for 4 seconds, exhale slowly for 6 seconds.", 120, "Longer exhales tell your body it is safe to relax.")
            )
        ),
        MovementRoutine(
            id = "relax_gentle_mobility",
            title = "Gentle Mobility",
            category = "RELAX",
            durationMinutes = 7,
            difficulty = "Gentle",
            focusTags = "Rest Day • Ease",
            description = "Perfect for low-energy days or rest days when you just want to move softly.",
            steps = listOf(
                ExerciseStep("Seated Cat-Cow", "Sit cross-legged and gently rock your spine forward and back.", 90, "Move like water."),
                ExerciseStep("Side Rib Reaches", "Sweep one arm overhead and lean softly to the side.", 90, "Switch sides at your own rhythm."),
                ExerciseStep("Knee-to-Chest Hug", "Lie on your back, hug both knees into your chest, and rock gently side to side.", 120, "Massaging your lower back against the floor."),
                ExerciseStep("Quiet Rest", "Stretch out long and take five deep, appreciative breaths.", 120, "Rest is part of taking care of yourself.")
            )
        )
    )
}

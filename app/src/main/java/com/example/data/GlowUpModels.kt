package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 1,
    val userName: String = "Astha",
    val studentClass: String = "Class 11",
    val schoolName: String = "High School • PCM + CS",
    val subjectsCsv: String = "Physics,Chemistry,Mathematics,Computer Science / AI,English",
    val goalsText: String = "Master PCM concepts, build meaningful prototypes (Krow, Smart EcoDrain Shield), prepare for NCSC & undergraduate scholarships, and stay healthy.",
    val interestsText: String = "AI & Computer Science, Startups, Sustainability Research, Anime, Fitness & Mobility",
    val customGreeting: String = "",
    val onboardingCompleted: Boolean = true,
    val selectedFocusAreas: String = "Move more,Stay hydrated,Build a skincare routine,Sleep better,Feel more confident",
    val freeTimeWindow: String = "Evening",
    val dailyTimeCommitmentMinutes: Int = 15,
    val currentEnergyLevel: String = "NORMAL", // LOW, NORMAL, HIGH
    val currentDayMode: String = "School day", // School day, Weekend, Exam preparation, Competition preparation
    val morningStudyStartTime: String = "6:00 AM",
    val morningStudyEndTime: String = "7:15 AM",
    val schoolStartTime: String = "7:15 AM",
    val schoolEndTime: String = "2:30 PM",
    val afterSchoolResetStartTime: String = "2:30 PM",
    val afterSchoolResetEndTime: String = "4:00 PM",
    val eveningStudyStartTime: String = "4:00 PM",
    val eveningStudyEndTime: String = "9:00 PM",
    val eveningPlanMode: String = "STANDARD", // STANDARD, SHORT, LOW_ENERGY
    val dronaSchedule: String = "", // Legacy field - No coaching attended; user uses self-paced Mission Jeet
    val wakeTime: String = "6:00 AM",
    val targetBedtime: String = "10:15 PM",
    val windDownTime: String = "9:40 PM",
    val studyResetEnabled: Boolean = true,
    val studyResetIntervalMinutes: Int = 45,
    val hydrationRemindersEnabled: Boolean = true,
    val hydrationPaused: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val morningNotificationEnabled: Boolean = true,
    val movementNotificationEnabled: Boolean = true,
    val skincareNotificationEnabled: Boolean = true,
    val sleepNotificationEnabled: Boolean = true,
    val confidenceNotificationEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val themeMode: String = "LIGHT", // LIGHT, DARK, SYSTEM
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val entertainmentTitle: String = "Anime",
    val entertainmentEpisodesAllowed: Int = 1,
    val entertainmentUnlockedToday: Boolean = false,
    val ncscEventName: String = "NCSC National Children's Science Congress",
    val ncscTargetDate: String = "2026-10-25",
    val ncscProjectTitle: String = "Smart EcoDrain Shield (Completed & Presented)",
    val ncscChecklistCsv: String = "Project understanding,Problem statement,Innovation,Prototype,Presentation Delivered",
    val ncscCompleted: Boolean = true,
    val halfYearlyStartDate: String = "2026-11-16",
    val activeStartupIdeaTitle: String = "High-Growth Working App Idea",
    val nextWeekFocus: String = "Solve 5 medium Physics numericals before checking solutions",
    val weeklyWorkedWell: String = "Mission Jeet active recall and problem sets right after lessons.",
    val weeklyDidNotWork: String = "Switching to random videos when stuck on numericals.",
    val weeklyShouldChange: String = "Use the 5-level I'm Stuck system and log formula mistakes immediately.",
    val lastRecommendedTaskId: Int = -1,
    val lastRecommendedSubject: String = "",
    val lastCompletedActivityType: String = "",
    val lastCompletedTimestamp: Long = 0L,
    val recommendationRotationCounter: Int = 0,
    val monthReviewWhatBuilt: String = "Krow functional mobile prototype & Smart EcoDrain Shield scale filtration model.",
    val monthReviewWhatLearned: String = "Rotational dynamics problem setup, Python recursion, and structured user feedback questioning.",
    val monthReviewWhatEvidence: String = "Krow GitHub repository, 3 user walkthrough observations, and EcoDrain flow-test logs.",
    val monthReviewContinue: String = "25–35m focused problem sets right after power reset.",
    val monthReviewStop: String = "Re-watching full one-shot lectures instead of practicing questions.",
    val weeklyPlanMon: String = "STUDY DAY • School + Evening Mission Jeet & Revision",
    val weeklyPlanTue: String = "REVISION DAY • Formula recall + Krow project",
    val weeklyPlanWed: String = "RESEARCH / EXPLORATION DAY • AI/CS curiosity + EcoDrain",
    val weeklyPlanThu: String = "STUDY DAY • School + Evening Mission Jeet Practice",
    val weeklyPlanFri: String = "COMPETITION PREPARATION DAY • NCSC & Krow validation",
    val weeklyPlanSat: String = "PROJECT DAY • Flexible build or personal experience",
    val weeklyPlanSun: String = "FAMILY TIME DAY • Review, hobbies & calm rest",
    val lastActiveDate: String = ""
)

@Entity(tableName = "mission_jeet_lectures")
data class MissionJeetLectureEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String, // Physics, Chemistry Physical, Chemistry Organic/Inorganic, Mathematics, Computer Science / AI
    val chapter: String,
    val topic: String,
    val lectureTitle: String,
    val lectureUrlOrRef: String = "",
    val totalDurationMinutes: Int = 90,
    val lastWatchedPositionText: String = "00:00", // e.g. "42:15"
    val watchedMinutes: Int = 0,
    val sectionsCompletedCsv: String = "", // e.g. "Intro,Concept derivation,Worked Example"
    val doubtsAndNotes: String = "",
    val conceptConfidence: String = "MEDIUM", // LOW, MEDIUM, HIGH
    val status: String = "NOT_STARTED", // NOT_STARTED, IN_PROGRESS, LESSON_COMPLETED, RECALL_PENDING, PRACTICE_PENDING, REVIEWED
    val recallCompleted: Boolean = false,
    val questionsAttemptedCount: Int = 0,
    val questionsSolvedCount: Int = 0,
    val mistakesIdentified: String = "",
    val revisionStatus: String = "PENDING", // PENDING, DONE
    val nextAction: String = "Watch first 25 minutes and take active notes",
    val isPriority: Boolean = false,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "hobbies_and_joy")
data class HobbyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String, // "Drawing & Sketching", "Anime & Entertainment", "Evening Walk", "Family Time & Dinner"
    val category: String = "CREATIVE", // CREATIVE, ENTERTAINMENT, PHYSICAL, SOCIAL
    val preferredWindow: String = "Evening or After-school", // After-school, Evening, Night, Weekend
    val suggestedDurationMinutes: Int = 30,
    val description: String = "",
    val isScheduledOnCalendar: Boolean = false,
    val lastEnjoyedDate: String = "",
    val notesOrCurrentFocus: String = "",
    val guiltFreeReminder: String = "Rest, joy, and creative play protect your long-term focus and curiosity."
)

@Entity(tableName = "os_tasks")
data class OsTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subtitle: String, // Concrete how-to instructions
    val whyItMatters: String,
    val domain: String, // ACADEMIC, PROJECT, WELLNESS, PERSONAL, COMPETITION, SCHOLARSHIP, PROFILE, RESEARCH, FAMILY
    val priorityBucket: String = "MUST DO", // MUST DO, SHOULD DO, CAN POSTPONE, CAN DELETE
    val isTopThree: Boolean = false,
    val topThreeSlot: Int = 0, // 1 = Academic, 2 = Project/Profile, 3 = Wellness/Self-care
    val durationMinutes: Int = 25,
    val energyRequired: String = "NORMAL", // LOW, NORMAL, HIGH
    val scheduledTimeSlot: String = "3:30 PM",
    val dateString: String = "",
    val isCompleted: Boolean = false,
    val isPostponed: Boolean = false,
    val subject: String = "",
    val linkedEventId: Int = -1,
    val prepMilestoneKey: String = "", // Prevents duplicate preparation tasks when refreshing/recalculating
    val endOfDayDecision: String = "", // RESCHEDULE, KEEP IN BACKLOG, DROP, DECIDE TOMORROW
    val lastRecommendedAt: Long = 0L,
    val lastCompletedAt: Long = 0L,
    val timesRecommended: Int = 0,
    val timesCompleted: Int = 0,
    val skipCount: Int = 0,
    val sortOrder: Int = 0
)

@Entity(tableName = "calendar_events")
data class CalendarEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val dateString: String, // Start date yyyy-MM-dd
    val endDateString: String = "", // End date yyyy-MM-dd for multi-day events (blank or == dateString if 1 day)
    val startTime: String = "9:00 AM",
    val endTime: String = "10:00 AM",
    val isAllDay: Boolean = false,
    val category: String = "ACADEMICS", // ACADEMICS, EXAMS / TESTS, SCHOOL, COACHING, COMPETITIONS, NCSC, HACKATHONS, SCHOLARSHIPS, APPLICATIONS, PROJECTS, RESEARCH, REVISION, REST / RECOVERY, FAMILY TIME, FRIENDS / SOCIAL, PERSONAL, WELLNESS, TRAVEL, OTHER
    val priority: String = "HIGH", // HIGH, MEDIUM, NORMAL
    val locationOrNotes: String = "",
    val location: String = "",
    val repeatRule: String = "None", // None, Daily, Weekly, Monthly
    val reminderEnabled: Boolean = true,
    val reminderPreference: String = "Day before & 1 hour before", // At chosen time, Before event, On event day, For prep milestones
    val reminderHour24: Int = 18,
    val reminderMinute: Int = 0,
    val linkedProjectOrGoal: String = "",
    val attendanceOrCompletionStatus: String = "PLANNED", // PLANNED, PREPARING, ATTENDED, COMPLETED, RESCHEDULED
    val beforeEventChecklistCsv: String = "Materials packed,Presentation script checked,Prototype tested,Travel & timing confirmed",
    val duringEventNotes: String = "",
    val afterEventReflection: String = "",
    val preparationStarted: Boolean = false,
    val preparationCompletedCount: Int = 0,
    val dismissedMilestoneDaysCsv: String = "" // tracks dismissed suggested prep days e.g. "5,4"
) {
    val effectiveEndDate: String
        get() = endDateString.ifBlank { dateString }

    val isMultiDay: Boolean
        get() = effectiveEndDate != dateString
}

@Entity(tableName = "special_day_plans")
data class SpecialDayPlanEntity(
    @PrimaryKey val dateString: String, // yyyy-MM-dd
    val dayType: String, // STUDY DAY, REVISION DAY, PROJECT DAY, RESEARCH / EXPLORATION DAY, COMPETITION PREPARATION DAY, SCHOLARSHIP / APPLICATION DAY, SKILL-BUILDING DAY, REST / RESET DAY, FAMILY TIME DAY, FRIENDS / SOCIAL DAY, OUTDOOR / EXPERIENCE DAY, BALANCED DAY, CUSTOM DAY
    val customFocusNote: String = "",
    val customStructureItemsCsv: String = "",
    val conflictOverrideApproved: Boolean = false
)

@Entity(tableName = "event_prep_milestones")
data class EventPrepMilestoneEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventId: Int,
    val eventTitle: String,
    val milestoneKey: String, // Unique per event + step (e.g. "ev_1_identify_syllabus") so it never duplicates
    val phase: String = "BEFORE", // BEFORE, DURING, AFTER
    val stepTitle: String,
    val stepInstruction: String,
    val targetDateString: String,
    val durationMinutes: Int = 20,
    val status: String = "SUGGESTED" // SUGGESTED, ACCEPTED, COMPLETED, POSTPONED, DISMISSED
)

@Entity(tableName = "end_of_day_reflections")
data class EndOfDayReflectionEntity(
    @PrimaryKey val dateString: String,
    val meaningfulToday: String = "",
    val learnedOrExperienced: String = "",
    val gladMadeTimeFor: String = "",
    val carryIntoTomorrow: String = "",
    val letGoOfForNow: String = "",
    val quickTagSelected: String = "", // "I rested.", "I spent time with my parents.", "I explored something interesting.", "I completed my revision.", "I had an ordinary day, and that's okay."
    val tomorrowFirstStep: String = "",
    val dayClosed: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "monthly_goals")
data class MonthlyGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val monthLabel: String = "October 2026",
    val domain: String, // ACADEMICS, PROJECT, COMPETITION, PROFILE, SKILLS, WELLNESS
    val title: String,
    val weeklyTarget: String,
    val todaySmallestStep: String,
    val linkedLongTermArea: String = "PROJECTS",
    val progressStepsDone: Int = 1,
    val progressStepsTotal: Int = 4,
    val reviewDecision: String = "MUST CONTINUE", // MUST CONTINUE, SHOULD CONTINUE, CAN POSTPONE, CAN DROP
    val isCompleted: Boolean = false
)

@Entity(tableName = "long_term_goals")
data class LongTermGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val area: String, // ACADEMICS, PROJECTS, RESEARCH / INNOVATION, COMPETITIONS, LEADERSHIP / IMPACT, TECHNICAL SKILLS, COMMUNICATION, SCHOLARSHIP / APPLICATION READINESS, WELLNESS
    val visionStatement: String,
    val class11Milestone: String,
    val class12Milestone: String,
    val currentMonthlyFocus: String,
    val weeklyTarget: String,
    val todayAction: String
)

@Entity(tableName = "recent_wins")
data class RecentWinEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val whatChangedDetail: String,
    val category: String, // STUDY, BUILD, PROFILE, WELLNESS, GOAL
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class DeadlinePreparationSuggestion(
    val eventId: Int,
    val eventTitle: String,
    val category: String,
    val daysRemaining: Int,
    val countdownCalmText: String,
    val suggestedTaskTitle: String,
    val suggestedTaskSubtitle: String,
    val durationMinutes: Int = 20
)

@Entity(tableName = "timeline_blocks")
data class TimelineBlockEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: String,
    val endTime: String,
    val title: String,
    val subtitle: String,
    val blockType: String, // FIXED, FOCUS, RESET, WELLNESS, MEAL
    val recoveryCategory: String = "MUST DO", // MUST DO, SHOULD DO, CAN POSTPONE, CAN DELETE
    val durationMinutes: Int = 45,
    val isCompleted: Boolean = false,
    val isSkipped: Boolean = false,
    val sortOrder: Int = 0
)

@Entity(tableName = "study_chapters")
data class StudyChapterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String, // Physics, Chemistry Physical, Chemistry Organic/Inorganic, Mathematics, Computer Science / AI
    val chapterName: String,
    val currentTopic: String,
    val weakAreas: String,
    val videoResource: String = "",
    val purpose: String = "Concept", // Concept, Full chapter, Revision, Weak topic
    val oneShotCompleted: Boolean = false,
    val capturedConcept: String = "",
    val capturedFormula: String = "",
    val capturedDoubt: String = "",
    val understandingStatus: String = "YELLOW", // GREEN, YELLOW, RED
    val questionsAttempted: Int = 0,
    val questionsSolved: Int = 0,
    val questionsPartial: Int = 0,
    val questionsWrong: Int = 0,
    val questionsSkipped: Int = 0,
    val dominantWeakness: String = "Formula selection", // Concept, Formula selection, Calculation, Application
    val lastStudiedTimestamp: Long = System.currentTimeMillis(),
    val specialStudyMaterial: String = "",
    val keyFormulasText: String = "",
    val examTrapsAndTricks: String = "",
    val flashcardsJson: String = "",
    val halfYearlyWeightage: String = "8 Marks"
)

data class ChapterFlashcard(
    val id: String = java.util.UUID.randomUUID().toString(),
    val question: String,
    val answer: String,
    val formulaOrTrick: String = "",
    val repetitions: Int = 0,
    val intervalDays: Int = 1,
    val easeFactor: Float = 2.5f,
    val nextReviewDate: String = "",
    val lastRating: String = "NEW" // NEW, AGAIN, HARD, GOOD, MASTERED
)

@Entity(tableName = "error_log_entries")
data class ErrorLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val chapter: String,
    val questionSummary: String,
    val mistakeType: String, // Concept, Formula, Approach, Calculation, Careless, Interpretation, Memory, Time management
    val whyIMadeIt: String,
    val correctIdea: String,
    val source: String = "Drona", // Study Panda, Mission Jeet, School, Drona, Book, Other
    val dateString: String,
    val createdTimestamp: Long = System.currentTimeMillis(),
    val reviewStage: Int = 1, // 1 = 24h, 2 = 3d, 3 = 7d, 4 = Mastered
    val nextReviewLabel: String = "Review in 24 hours",
    val isResolved: Boolean = false
)

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val tagline: String,
    val pipelineStage: String, // IDEA, VALIDATE, MVP, TEST, ITERATE, EVIDENCE, PITCH, IMPACT
    val problemStatement: String,
    val targetUser: String,
    val solutionSummary: String,
    val prototypeDetails: String,
    val technologyStack: String,
    val myRole: String,
    val testingSummary: String,
    val userFeedbackSummary: String,
    val impactSummary: String,
    val githubOrDemoUrl: String,
    val competitionTarget: String,
    val status: String,
    val nextConcreteAction: String,
    // 9 Evidence flags — never fake numbers
    val evProblemDocumented: Boolean = false,
    val evPrototypeExists: Boolean = false,
    val evDemoAvailable: Boolean = false,
    val evGithubAvailable: Boolean = false,
    val evUserFeedback: Boolean = false,
    val evTestingEvidence: Boolean = false,
    val evPresentation: Boolean = false,
    val evImpactEvidence: Boolean = false,
    val evCompetitionSubmission: Boolean = false
) {
    val evidenceCheckedCount: Int
        get() = listOf(
            evProblemDocumented,
            evPrototypeExists,
            evDemoAvailable,
            evGithubAvailable,
            evUserFeedback,
            evTestingEvidence,
            evPresentation,
            evImpactEvidence,
            evCompetitionSubmission
        ).count { it }

    val evidenceStrengthLabel: String
        get() = when {
            evidenceCheckedCount >= 6 -> "STRONG"
            evidenceCheckedCount >= 3 -> "BUILDING"
            else -> "NEEDS EVIDENCE"
        }
}

@Entity(tableName = "profile_achievements")
data class ProfileAchievementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // ACADEMICS, PROJECTS, RESEARCH, COMPETITIONS, LEADERSHIP, COMMUNITY IMPACT, TECHNICAL SKILLS, CERTIFICATIONS, AWARDS, SPORTS / ACTIVITIES, COMMUNICATION, ESSAYS, RECOMMENDATIONS, PORTFOLIO
    val title: String,
    val dateString: String,
    val organization: String,
    val role: String,
    val whatIDid: String,
    val resultSummary: String,
    val impactSummary: String,
    val evidenceNote: String,
    val linkUrl: String = ""
)

@Entity(tableName = "opportunities")
data class OpportunityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // Scholarships, Competitions, Research, Summer programs, Hackathons, Innovation programs, Leadership, International opportunities
    val country: String,
    val organization: String,
    val eligibility: String,
    val deadline: String,
    val funding: String,
    val applicationRequirements: String,
    val officialSource: String,
    val status: String, // Saved, Researching, Eligible, Preparing, Submitted, Result
    val notes: String,
    val isVerified: Boolean = false,
    val lastVerifiedDate: String = "Unverified — verify on official portal"
)

@Entity(tableName = "brain_dump_entries")
data class BrainDumpEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val category: String, // Study, Project, Scholarship, Personal, Tomorrow, Random
    val organizedBucket: String, // Tomorrow -> tasks, Long-term -> backlog, Important -> priority, Random -> notes
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis(),
    val convertedToTask: Boolean = false
)

@Entity(tableName = "distraction_logs")
data class DistractionLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskTitle: String,
    val focusRating: String, // Fully focused, Mostly focused, Distracted
    val distractionSource: String, // YouTube, Anime, Social media, Random browsing, Unclear task, Tiredness, Phone, Other, None
    val durationMinutes: Int,
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_habits")
data class DailyHabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // HYDRATE, MOVE, GLOW, SLEEP, CONFIDENCE, MEALS, POSTURE, CUSTOM
    val title: String,
    val subtitle: String,
    val emoji: String,
    val completedToday: Boolean = false,
    val isActive: Boolean = true,
    val sortOrder: Int = 0
)

@Entity(tableName = "skincare_steps")
data class SkincareStepEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val period: String, // MORNING, EVENING
    val title: String,
    val completedToday: Boolean = false,
    val sortOrder: Int = 0
)

@Entity(tableName = "hydration_reminders")
data class HydrationReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timeLabel: String,
    val hour24: Int,
    val minute: Int,
    val isEnabled: Boolean = true,
    val completedToday: Boolean = false
)

@Entity(tableName = "confidence_entries")
data class ConfidenceEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis(),
    val moodEmoji: String,
    val moodLabel: String,
    val proudOfText: String, // "How did you show up today?"
    val helpedFeelGoodText: String
)

@Entity(tableName = "daily_progress_history")
data class DailyProgressEntity(
    @PrimaryKey val dateString: String,
    val movementCompleted: Boolean = false,
    val hydrationCompletedCount: Int = 0,
    val morningSkincareCompleted: Boolean = false,
    val eveningSkincareCompleted: Boolean = false,
    val sleepWindDownCompleted: Boolean = false,
    val confidenceCheckInCompleted: Boolean = false,
    val missedBedtimeAcknowledged: Boolean = false
)

data class ExerciseStep(
    val name: String,
    val instruction: String,
    val durationSeconds: Int,
    val breathCue: String
)

data class MovementRoutine(
    val id: String,
    val title: String,
    val category: String, // QUICK RESET, NORMAL, FULL, LOW ENERGY, POSTURE
    val durationMinutes: Int,
    val difficulty: String,
    val focusTags: String,
    val description: String,
    val steps: List<ExerciseStep>
)

data class PracticeQuestionItem(
    val id: String,
    val subject: String,
    val chapter: String,
    val difficulty: String, // Easy, Medium, Hard
    val source: String, // Study Panda, Mission Jeet, School, Drona, Book
    val focusSkill: String, // Formula selection, Calculation, Application, Concept
    val questionText: String,
    val givenPrompt: String,
    val askedPrompt: String,
    val conceptPrompt: String,
    val hint1Concept: String,
    val hint2Formula: String,
    val hint3Strategy: String,
    val step4FirstStep: String,
    val step5FullSolution: String
)

data class NowRecommendation(
    val title: String,
    val subtitleShort: String = "Get one piece of user feedback.",
    val humanHeadline: String = "Your Krow prototype is moving forward, and 1 user note will unlock validation.",
    val concreteAction: String,
    val whyReason: String,
    val durationMinutes: Int,
    val currentTimeFormatted: String,
    val contextualLine: String = "Your day is still yours.",
    val categoryBadge: String = "YOUR NEXT MOVE",
    val energyLevel: String,
    val availableMinutes: Int,
    val priorityDomain: String,
    val subjectOrProject: String = "Krow",
    val alternateOptions: List<String> = listOf(
        "Chemistry • 15 min revision",
        "NCSC • 10 min speaking practice",
        "Wellness • 10 min mobility reset"
    ),
    val selectedDayType: String = "BALANCED DAY",
    val conflictWarningMessage: String? = null,
    val taskToStart: OsTaskEntity? = null,
    val chapterToStudy: StudyChapterEntity? = null,
    val projectToBuild: ProjectEntity? = null
)

data class GeneratedRoutineBlock(
    val periodTitle: String,
    val emoji: String,
    val items: List<String>
)

data class AiChatMessage(
    val id: Long = System.currentTimeMillis(),
    val isUser: Boolean,
    val text: String,
    val actionChipLabel: String? = null
)

@Entity(tableName = "setback_recoveries")
data class SetbackRecoveryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateString: String,
    val eventTitle: String,
    val category: String = "ACADEMIC", // ACADEMIC, COMPETITION, APPLICATION, PROJECT, SOCIAL
    val whatActuallyHappened: String,
    val howAmIFeeling: String,
    val whatIsStillTrueAboutMe: String,
    val whatWasOutOfMyControl: String,
    val whatWasInMyControl: String,
    val actionableLesson: String,
    val gentleNextStep: String,
    val isResolved: Boolean = false
)

@Entity(tableName = "financial_experiments")
data class FinancialExperimentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val skillUsed: String, // Full-stack coding, Technical writing, Math tutoring, UI design, Data synthesis
    val experimentStage: String = "IDEATION", // IDEATION, OUTREACH, IN_PROGRESS, DELIVERED, COMPLETED
    val estimatedEarningPotential: String = "₹500 - ₹2,000",
    val actualEarned: Double = 0.0,
    val clientOrPlatform: String = "",
    val whatLearnedAboutValue: String = "",
    val dateString: String = ""
)

data class ConfidenceLabScenario(
    val id: String,
    val title: String,
    val category: String, // Class Participation, Project Pitch, Handling Feedback, Setting Boundaries, Networking
    val difficulty: String, // Gentle, Moderate, Stretched
    val contextDescription: String,
    val commonMistakeOrTrap: String,
    val coreMindsetShift: String,
    val recommendedScript: String,
    val bodyLanguageCue: String,
    val practicePrompt: String,
    val reflectionQuestions: List<String>
)

data class FinancialLesson(
    val id: String,
    val title: String,
    val subtitle: String,
    val tag: String,
    val coreConcept: String,
    val teenTakeaway: String,
    val actionableExercise: String
)

@Entity(tableName = "academic_topics")
data class AcademicTopicEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String, // Physics, Chemistry Physical, Chemistry Organic/Inorganic, Mathematics, Computer Science & AI
    val chapterName: String,
    val topicName: String,
    val syllabusType: String = "CURRENT", // CURRENT, EXAM_SYLLABUS
    val isConceptUnderstood: Boolean = false,
    val hasIndependentRecall: Boolean = false,
    val practiceLevel: String = "NOT_STARTED", // NOT_STARTED, LESSON_WATCHED, CONCEPT_UNDERSTOOD, INDEPENDENT_RECALL, SCHOOL_LEVEL, EXAM_LEVEL, MASTERED
    val mistakePatterns: String = "",
    val lastRevisionDate: String = "",
    val nextRevisionDate: String = "",
    val requiresTeacherGuidance: Boolean = false,
    val teacherGuidanceNotes: String = "",
    val isChemistryFullMarksFocus: Boolean = false,
    val definitionsAndReactionsNotes: String = "",
    val calculationsAndNumericalsNotes: String = "",
    val timedPaperScore: String = "",
    val dateAdded: String = ""
)

@Entity(tableName = "science_exhibition_scorecards")
data class ScienceExhibitionScorecardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ideaTitle: String,
    val exhibitionTheme: String = "Science & Tech for Society",
    val problemImportanceScore: Int = 7, // 1-10
    val evidenceProblemExistsScore: Int = 7, // 1-10
    val existingSolutionsComparison: String = "",
    val genuineDifferentiation: String = "",
    val scientificCorrectnessScore: Int = 8, // 1-10
    val budgetRequiredInr: Double = 1500.0,
    val isBudgetFeasible: Boolean = true,
    val estimatedTimeDays: Int = 21,
    val workingDemoPossibilityScore: Int = 8, // 1-10
    val measurableResultsScore: Int = 7, // 1-10
    val safetyAndEnvironmentScore: Int = 9, // 1-10
    val easeOfExplainingToJudgesScore: Int = 8, // 1-10
    val potentialWeaknessesAndQuestions: String = "",
    val unknownInformation: String = "", // Marked as unknown rather than invented
    val isSelectedForExecution: Boolean = false,
    val executionMilestone: Int = 1, // 1..10
    val stopOrPivotStatus: String = "PROCEED" // PROCEED, PIVOT, STOP
) {
    val totalScore: Int
        get() = problemImportanceScore + evidenceProblemExistsScore + scientificCorrectnessScore +
                workingDemoPossibilityScore + measurableResultsScore + safetyAndEnvironmentScore + easeOfExplainingToJudgesScore
}

@Entity(tableName = "scholarship_strategies")
data class ScholarshipStrategyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val country: String,
    val degreeSubjectEligibility: String = "STEM / Undergraduate",
    val nationalityRequirements: String = "Indian Citizen / Open International",
    val academicRequirements: String = "Class 11/12 High Academic Standing (85%+)",
    val ageAndCompletionRequirements: String = "17-21 Years; Completed 12 years of formal schooling",
    val languageRequirements: String = "English proficiency / Relevant language",
    val requiredExaminations: String = "Document review, Subject exam or SAT/EJU if applicable",
    val fundingCoverage: String = "Full Ride (Tuition + Living + Travel)", // Tuition only vs Full Ride (Tuition + Living + Travel)
    val isFullyFunded: Boolean = true,
    val excludedCosts: String = "Visa application, personal travel outside program",
    val applicationFeeVerified: String = "None (Verified)",
    val applicationOpening: String = "",
    val applicationDeadline: String = "",
    val requiredDocuments: String = "",
    val recommendationLetters: String = "",
    val selectionProcess: String = "",
    val officialSourceLink: String = "",
    val lastVerifiedDate: String = "Verified Oct 2026",
    val eligibilityStatus: String = "ELIGIBLE", // ELIGIBLE, NEEDS_VERIFICATION, INELIGIBLE_FOR_CURRENT_CYCLE, PREPARING
    val nextAction: String = "",
    val uncertaintiesToResolve: String = "",
    val maxAcceptableFamilyCostInr: Double = 0.0,
    val isMextSpecific: Boolean = false,
    val applicationYear: String = "2027",
    val universityEntryYear: String = "2028",
    val cycleOfficialStatusNotice: String = ""
)

@Entity(tableName = "scenario_plans")
data class ScenarioPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalCategory: String, // EXAM, SCIENCE_EXHIBITION, SCHOLARSHIP, PROJECT
    val title: String,
    val bestCaseOutcome: String,
    val bestCaseEvidenceToSupport: String,
    val bestCaseActionToMaximize: String,
    val expectedCaseOutcome: String,
    val expectedCaseRealisticPlan: String,
    val setbackCaseRisk: String,
    val setbackPreventiveAction: String,
    val setbackRecoveryAlternative: String,
    val keyAssumptions: String,
    val earlyWarningSigns: String,
    val nextDecisionPoint: String
)

@Entity(tableName = "profile_evidence_ledger")
data class ProfileEvidenceLedgerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // PROTOTYPE, EXPERIMENT, USER_FEEDBACK, GITHUB, PRESENTATION, RESEARCH, COURSE, COMPETITION, LEADERSHIP, ACADEMIC
    val status: String = "COMPLETED", // PLANNED, IN_PROGRESS, COMPLETED, EXTERNALLY_VERIFIED
    val dateString: String = "",
    val evidenceLinkOrFile: String = "",
    val whatWasLearnedAndDemonstrated: String = "",
    val isExternallyVerified: Boolean = false
)

@Entity(tableName = "anger_overwhelm_pauses")
data class AngerOverwhelmPauseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val triggerEvent: String,
    val firstImpulseOrEmotion: String,
    val factualRealityCheck: String,
    val alternativeInterpretation1: String,
    val alternativeInterpretation2: String,
    val chosenConstructiveResponse: String,
    val dateString: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class MorningStudyPriority(
    val priorityRank: Int, // 1, 2, 3
    val subject: String,
    val exactTopic: String,
    val startingResourceOrMethod: String,
    val activeRecallStep: String,
    val questionsToAttempt: String,
    val completionCriterion: String,
    val nextStepIfStuck: String,
    val estimatedMinutes: Int = 30
)

data class MorningDecisionPlan(
    val mostImportantThingToday: String,
    val startingSubject: String,
    val startingExactTopic: String,
    val whatToDoAfterStudying: String,
    val nextPriorityAfterThat: String,
    val whatCanWaitUntilAnotherDay: String,
    val priorities: List<MorningStudyPriority>,
    val availableMinutes: Int = 120,
    val energyLevel: String = "NORMAL",
    val generatedDateString: String = ""
)



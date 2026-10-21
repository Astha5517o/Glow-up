package com.example.data

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GlowUpRepository(private val dao: GlowUpDao) {

    val userPreferences: Flow<UserPreferencesEntity?> = dao.getUserPreferences()
    val allTasks: Flow<List<OsTaskEntity>> = dao.getAllTasks()
    val timelineBlocks: Flow<List<TimelineBlockEntity>> = dao.getTimelineBlocks()
    val studyChapters: Flow<List<StudyChapterEntity>> = dao.getStudyChapters()
    val errorLogs: Flow<List<ErrorLogEntity>> = dao.getErrorLogs()
    val projects: Flow<List<ProjectEntity>> = dao.getProjects()
    val profileAchievements: Flow<List<ProfileAchievementEntity>> = dao.getProfileAchievements()
    val opportunities: Flow<List<OpportunityEntity>> = dao.getOpportunities()
    val brainDumps: Flow<List<BrainDumpEntity>> = dao.getBrainDumps()
    val distractionLogs: Flow<List<DistractionLogEntity>> = dao.getDistractionLogs()
    val calendarEvents: Flow<List<CalendarEventEntity>> = dao.getCalendarEvents()
    val monthlyGoals: Flow<List<MonthlyGoalEntity>> = dao.getMonthlyGoals()
    val longTermGoals: Flow<List<LongTermGoalEntity>> = dao.getLongTermGoals()
    val recentWins: Flow<List<RecentWinEntity>> = dao.getRecentWins()
    val specialDayPlans: Flow<List<SpecialDayPlanEntity>> = dao.getSpecialDayPlans()
    val eventPrepMilestones: Flow<List<EventPrepMilestoneEntity>> = dao.getEventPrepMilestones()
    val endOfDayReflections: Flow<List<EndOfDayReflectionEntity>> = dao.getEndOfDayReflections()
    val setbackRecoveries: Flow<List<SetbackRecoveryEntity>> = dao.getSetbackRecoveries()
    val financialExperiments: Flow<List<FinancialExperimentEntity>> = dao.getFinancialExperiments()
    val academicTopics: Flow<List<AcademicTopicEntity>> = dao.getAcademicTopics()
    val scienceExhibitionScorecards: Flow<List<ScienceExhibitionScorecardEntity>> = dao.getScienceExhibitionScorecards()
    val scholarshipStrategies: Flow<List<ScholarshipStrategyEntity>> = dao.getScholarshipStrategies()
    val scenarioPlans: Flow<List<ScenarioPlanEntity>> = dao.getScenarioPlans()
    val profileEvidenceLedger: Flow<List<ProfileEvidenceLedgerEntity>> = dao.getProfileEvidenceLedger()
    val angerOverwhelmPauses: Flow<List<AngerOverwhelmPauseEntity>> = dao.getAngerOverwhelmPauses()

    val activeHabits: Flow<List<DailyHabitEntity>> = dao.getActiveHabits()
    val skincareSteps: Flow<List<SkincareStepEntity>> = dao.getSkincareSteps()
    val hydrationReminders: Flow<List<HydrationReminderEntity>> = dao.getHydrationReminders()
    val confidenceEntries: Flow<List<ConfidenceEntryEntity>> = dao.getConfidenceEntries()
    val recentProgressHistory: Flow<List<DailyProgressEntity>> = dao.getRecentProgressHistory()

    fun todayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    suspend fun initializeDefaultsIfNeeded() {
        val today = todayDateString()
        var prefs = dao.getUserPreferencesOnce()
        if (prefs == null) {
            prefs = UserPreferencesEntity(lastActiveDate = today, onboardingCompleted = true)
            dao.saveUserPreferences(prefs)
        }

        if (dao.getAllTasksOnce().isEmpty()) {
            dao.insertTasks(DefaultWellnessData.defaultOsTasks(today))
        }

        if (dao.getTimelineBlocksOnce().isEmpty()) {
            dao.insertTimelineBlocks(DefaultWellnessData.defaultTimelineBlocks())
        }

        if (dao.getStudyChaptersOnce().isEmpty()) {
            dao.insertStudyChapters(DefaultWellnessData.defaultStudyChapters())
        }

        if (dao.getErrorLogsOnce().isEmpty()) {
            dao.insertErrorLogs(DefaultWellnessData.defaultErrorLogs(today))
        }

        if (dao.getProjectsOnce().isEmpty()) {
            dao.insertProjects(DefaultWellnessData.defaultProjects())
        }

        if (dao.getProfileAchievementsOnce().isEmpty()) {
            dao.insertProfileAchievements(DefaultWellnessData.defaultProfileAchievements())
        }

        if (dao.getOpportunitiesOnce().isEmpty()) {
            dao.insertOpportunities(DefaultWellnessData.defaultOpportunities())
        }

        if (dao.getAllHabitsOnce().isEmpty()) {
            dao.insertHabits(
                DefaultWellnessData.defaultHabits(
                    moveDuration = prefs.dailyTimeCommitmentMinutes,
                    windDownTime = prefs.windDownTime
                )
            )
        }

        if (dao.getSkincareStepsOnce().isEmpty()) {
            dao.insertSkincareSteps(DefaultWellnessData.defaultSkincareSteps())
        }

        if (dao.getHydrationRemindersOnce().isEmpty()) {
            dao.insertHydrationReminders(DefaultWellnessData.defaultHydrationReminders())
        }

        if (dao.getCalendarEventsOnce().isEmpty()) {
            dao.insertCalendarEvents(DefaultWellnessData.defaultCalendarEvents(today))
        }

        if (dao.getMonthlyGoalsOnce().isEmpty()) {
            dao.insertMonthlyGoals(DefaultWellnessData.defaultMonthlyGoals())
        }

        if (dao.getLongTermGoalsOnce().isEmpty()) {
            dao.insertLongTermGoals(DefaultWellnessData.defaultLongTermGoals())
        }

        if (dao.getRecentWinsOnce().isEmpty()) {
            dao.insertRecentWins(DefaultWellnessData.defaultRecentWins(today))
        }

        if (dao.getSpecialDayPlansOnce().isEmpty()) {
            dao.insertSpecialDayPlans(DefaultWellnessData.defaultSpecialDayPlans(today))
        }

        if (dao.getEventPrepMilestonesOnce().isEmpty()) {
            dao.insertEventPrepMilestones(DefaultWellnessData.defaultEventPrepMilestones(today))
        }

        if (dao.getSetbackRecoveriesOnce().isEmpty()) {
            dao.insertSetbackRecoveries(DefaultWellnessData.defaultSetbackRecoveries(today))
        }

        if (dao.getFinancialExperimentsOnce().isEmpty()) {
            dao.insertFinancialExperiments(DefaultWellnessData.defaultFinancialExperiments(today))
        }

        if (dao.getAcademicTopicsOnce().isEmpty()) {
            dao.insertAcademicTopics(DefaultWellnessData.defaultAcademicTopics(today))
        }

        if (dao.getScienceExhibitionScorecardsOnce().isEmpty()) {
            dao.insertScienceExhibitionScorecards(DefaultWellnessData.defaultScienceExhibitionScorecards())
        }

        if (dao.getScholarshipStrategiesOnce().isEmpty()) {
            dao.insertScholarshipStrategies(DefaultWellnessData.defaultScholarshipStrategies())
        }

        if (dao.getScenarioPlansOnce().isEmpty()) {
            dao.insertScenarioPlans(DefaultWellnessData.defaultScenarioPlans())
        }

        if (dao.getProfileEvidenceLedgerOnce().isEmpty()) {
            dao.insertProfileEvidenceItems(DefaultWellnessData.defaultProfileEvidenceLedger(today))
        }

        if (dao.getAngerOverwhelmPausesOnce().isEmpty()) {
            dao.insertAngerOverwhelmPauses(DefaultWellnessData.defaultAngerOverwhelmPauses(today))
        }


        val history = dao.getAllProgressHistoryOnce()
        if (history.isEmpty()) {
            val cal = java.util.Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            for (daysAgo in 6 downTo 1) {
                cal.time = Date()
                cal.add(java.util.Calendar.DAY_OF_YEAR, -daysAgo)
                val dateStr = sdf.format(cal.time)
                dao.saveDailyProgress(
                    DailyProgressEntity(
                        dateString = dateStr,
                        movementCompleted = daysAgo % 2 != 0,
                        hydrationCompletedCount = 4,
                        morningSkincareCompleted = true,
                        eveningSkincareCompleted = daysAgo != 3,
                        sleepWindDownCompleted = daysAgo != 4,
                        confidenceCheckInCompleted = daysAgo <= 4
                    )
                )
            }
            dao.saveDailyProgress(DailyProgressEntity(dateString = today))
        }

        // Intelligent Daily Reset at midnight / next day:
        // Carry forward unfinished MUST DO & SHOULD DO tasks without duplicating
        if (prefs.lastActiveDate.isNotEmpty() && prefs.lastActiveDate != today) {
            dao.resetAllHabitsForToday()
            dao.resetSkincareForToday()
            dao.resetHydrationForToday()
            val existingTasks = dao.getAllTasksOnce()
            existingTasks.forEach { task ->
                if (task.isCompleted && task.isTopThree) {
                    // Reset Top 3 daily recurring slots for the new day
                    dao.updateTask(task.copy(isCompleted = false, isPostponed = false, dateString = today))
                } else if (!task.isCompleted) {
                    dao.updateTask(task.copy(isPostponed = false, dateString = today))
                }
            }
            val blocks = dao.getTimelineBlocksOnce()
            blocks.forEach { block ->
                dao.updateTimelineBlock(block.copy(isCompleted = false, isSkipped = false))
            }
            dao.saveUserPreferences(
                prefs.copy(
                    lastActiveDate = today,
                    entertainmentUnlockedToday = false
                )
            )
            if (dao.getProgressForDate(today) == null) {
                dao.saveDailyProgress(DailyProgressEntity(dateString = today))
            }
        }
    }

    suspend fun savePreferences(prefs: UserPreferencesEntity) {
        dao.saveUserPreferences(prefs)
    }

    suspend fun completeOnboarding(
        userName: String,
        focusAreas: Set<String>,
        freeTime: String,
        durationMinutes: Int
    ) {
        val current = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
        val updated = current.copy(
            userName = userName.ifBlank { "Astha" },
            onboardingCompleted = true,
            selectedFocusAreas = focusAreas.joinToString(","),
            freeTimeWindow = freeTime,
            dailyTimeCommitmentMinutes = durationMinutes,
            lastActiveDate = todayDateString()
        )
        dao.saveUserPreferences(updated)
    }

    // OS Tasks
    suspend fun addTask(
        title: String,
        subtitle: String,
        whyItMatters: String,
        domain: String,
        priorityBucket: String,
        durationMinutes: Int,
        energyRequired: String,
        scheduledTimeSlot: String,
        isTopThree: Boolean = false,
        topThreeSlot: Int = 0
    ) {
        val current = dao.getAllTasksOnce()
        // Prevent exact duplicate active tasks
        if (current.any { it.title.equals(title.trim(), ignoreCase = true) && !it.isCompleted }) {
            return
        }
        dao.insertTask(
            OsTaskEntity(
                title = title.trim(),
                subtitle = subtitle.trim().ifBlank { "Complete one focused ${durationMinutes}-minute step." },
                whyItMatters = whyItMatters.trim().ifBlank { "Supports your ${domain.lowercase()} priorities." },
                domain = domain,
                priorityBucket = priorityBucket,
                isTopThree = isTopThree,
                topThreeSlot = topThreeSlot,
                durationMinutes = durationMinutes,
                energyRequired = energyRequired,
                scheduledTimeSlot = scheduledTimeSlot,
                dateString = todayDateString(),
                sortOrder = current.size + 1
            )
        )
    }

    suspend fun toggleTaskCompleted(task: OsTaskEntity) {
        val newCompleted = !task.isCompleted
        val nowMs = System.currentTimeMillis()
        dao.updateTask(
            task.copy(
                isCompleted = newCompleted,
                isPostponed = false,
                lastCompletedAt = if (newCompleted) nowMs else task.lastCompletedAt,
                timesCompleted = if (newCompleted) task.timesCompleted + 1 else task.timesCompleted
            )
        )

        // Also mark matching timeline block complete if applicable
        val blocks = dao.getTimelineBlocksOnce()
        blocks.firstOrNull {
            it.title.contains(task.subject.ifBlank { task.title.take(8) }, ignoreCase = true)
        }?.let { b ->
            dao.updateTimelineBlock(b.copy(isCompleted = newCompleted))
        }

        if (newCompleted) {
            val prefs = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
            val subjOrDomain = task.subject.ifBlank { task.domain }
            dao.saveUserPreferences(
                prefs.copy(
                    lastRecommendedTaskId = task.id,
                    lastRecommendedSubject = subjOrDomain,
                    lastCompletedActivityType = subjOrDomain,
                    lastCompletedTimestamp = nowMs,
                    recommendationRotationCounter = prefs.recommendationRotationCounter + 1,
                    entertainmentUnlockedToday = if (task.domain == "ACADEMIC" || task.topThreeSlot == 1) true else prefs.entertainmentUnlockedToday
                )
            )
            dao.insertRecentWin(
                RecentWinEntity(
                    title = task.title.take(36),
                    whatChangedDetail = "${task.subject.ifBlank { task.domain }}: ${task.subtitle.take(65)}",
                    category = task.domain,
                    dateString = todayDateString()
                )
            )
        }
    }

    suspend fun postponeTask(task: OsTaskEntity) {
        dao.updateTask(
            task.copy(
                isPostponed = true,
                priorityBucket = "CAN POSTPONE",
                scheduledTimeSlot = "Tomorrow",
                skipCount = task.skipCount + 1,
                lastRecommendedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun skipTaskForNow(task: OsTaskEntity?, currentSubjectOrProject: String = "") {
        val prefs = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
        val nowMs = System.currentTimeMillis()
        if (task != null) {
            dao.updateTask(
                task.copy(
                    skipCount = task.skipCount + 1,
                    timesRecommended = task.timesRecommended + 1,
                    lastRecommendedAt = nowMs
                )
            )
        }
        dao.saveUserPreferences(
            prefs.copy(
                lastRecommendedTaskId = task?.id ?: prefs.lastRecommendedTaskId,
                lastRecommendedSubject = currentSubjectOrProject.ifBlank {
                    task?.subject?.ifBlank { task.title.take(10) } ?: prefs.lastRecommendedSubject
                },
                recommendationRotationCounter = prefs.recommendationRotationCounter + 1
            )
        )
    }

    suspend fun markRecommendationCompleted(task: OsTaskEntity?, subjectOrDomain: String, whatChangedSummary: String = "") {
        val prefs = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
        val nowMs = System.currentTimeMillis()
        if (task != null && !task.isCompleted) {
            toggleTaskCompleted(task)
        } else {
            dao.insertRecentWin(
                RecentWinEntity(
                    title = subjectOrDomain.take(36),
                    whatChangedDetail = whatChangedSummary.ifBlank { "$subjectOrDomain: Focus block completed" },
                    category = "FOCUS",
                    dateString = todayDateString()
                )
            )
        }
        dao.saveUserPreferences(
            prefs.copy(
                lastRecommendedTaskId = task?.id ?: -1,
                lastRecommendedSubject = subjectOrDomain,
                lastCompletedActivityType = subjectOrDomain,
                lastCompletedTimestamp = nowMs,
                recommendationRotationCounter = prefs.recommendationRotationCounter + 1,
                entertainmentUnlockedToday = true
            )
        )
    }

    suspend fun rescheduleTask(task: OsTaskEntity, newTimeSlot: String, newPriorityBucket: String) {
        dao.updateTask(
            task.copy(
                scheduledTimeSlot = newTimeSlot,
                priorityBucket = newPriorityBucket,
                isPostponed = false
            )
        )
    }

    suspend fun deleteTask(id: Int) {
        dao.deleteTask(id)
    }

    // Smart Timeline & Recovery Engine ("Let's reset from here.")
    suspend fun toggleTimelineBlockCompleted(block: TimelineBlockEntity) {
        dao.updateTimelineBlock(block.copy(isCompleted = !block.isCompleted, isSkipped = false))
    }

    suspend fun skipTimelineBlock(block: TimelineBlockEntity) {
        dao.updateTimelineBlock(block.copy(isSkipped = true, isCompleted = false))
    }

    suspend fun addTimelineBlock(
        startTime: String,
        endTime: String,
        title: String,
        subtitle: String,
        blockType: String,
        recoveryCategory: String,
        durationMinutes: Int
    ) {
        val all = dao.getTimelineBlocksOnce()
        dao.insertTimelineBlock(
            TimelineBlockEntity(
                startTime = startTime,
                endTime = endTime,
                title = title,
                subtitle = subtitle,
                blockType = blockType,
                recoveryCategory = recoveryCategory,
                durationMinutes = durationMinutes,
                sortOrder = all.size + 1
            )
        )
    }

    suspend fun resetAndRecalculateRemainingDay(energyLevel: String) {
        val tasks = dao.getAllTasksOnce()
        tasks.forEach { task ->
            if (!task.isCompleted) {
                when (task.priorityBucket) {
                    "CAN DELETE" -> dao.deleteTask(task.id)
                    "CAN POSTPONE" -> dao.updateTask(
                        task.copy(isPostponed = true, scheduledTimeSlot = "Moved to tomorrow")
                    )
                    "SHOULD DO" -> {
                        if (energyLevel == "LOW" && !task.isTopThree) {
                            dao.updateTask(task.copy(isPostponed = true, scheduledTimeSlot = "Postponed (Low-energy recovery)"))
                        }
                    }
                    "MUST DO" -> {
                        val adaptedMins = if (energyLevel == "LOW") minOf(25, task.durationMinutes) else task.durationMinutes
                        dao.updateTask(task.copy(durationMinutes = adaptedMins, isPostponed = false))
                    }
                }
            }
        }
    }

    // Study Chapters & One-Shot Learning Mode
    suspend fun saveOneShotCapture(
        chapterId: Int,
        videoResource: String,
        purpose: String,
        capturedConcept: String,
        capturedFormula: String,
        capturedDoubt: String,
        understandingStatus: String
    ) {
        val chapters = dao.getStudyChaptersOnce()
        val target = chapters.find { it.id == chapterId } ?: return
        dao.updateStudyChapter(
            target.copy(
                videoResource = videoResource,
                purpose = purpose,
                oneShotCompleted = true,
                capturedConcept = capturedConcept,
                capturedFormula = capturedFormula,
                capturedDoubt = capturedDoubt,
                understandingStatus = understandingStatus,
                lastStudiedTimestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun addStudyChapter(
        subject: String,
        chapterName: String,
        currentTopic: String,
        weakAreas: String
    ) {
        dao.insertStudyChapter(
            StudyChapterEntity(
                subject = subject,
                chapterName = chapterName.trim(),
                currentTopic = currentTopic.trim().ifBlank { "Core Concepts & Numericals" },
                weakAreas = weakAreas.trim().ifBlank { "Formula application" }
            )
        )
    }

    suspend fun recordQuestionOutcome(
        subject: String,
        outcome: String, // SOLVED, PARTIAL, WRONG, SKIPPED
        weaknessDetected: String? = null
    ) {
        val chapters = dao.getStudyChaptersOnce()
        val target = chapters.firstOrNull { it.subject.equals(subject, ignoreCase = true) }
            ?: chapters.firstOrNull() ?: return

        val updated = target.copy(
            questionsAttempted = target.questionsAttempted + 1,
            questionsSolved = target.questionsSolved + if (outcome == "SOLVED") 1 else 0,
            questionsPartial = target.questionsPartial + if (outcome == "PARTIAL") 1 else 0,
            questionsWrong = target.questionsWrong + if (outcome == "WRONG") 1 else 0,
            questionsSkipped = target.questionsSkipped + if (outcome == "SKIPPED") 1 else 0,
            dominantWeakness = weaknessDetected ?: target.dominantWeakness,
            lastStudiedTimestamp = System.currentTimeMillis()
        )
        dao.updateStudyChapter(updated)
    }

    // Error Log with Spaced Review (24h -> 3d -> 7d -> Mastered)
    suspend fun addErrorLog(
        subject: String,
        chapter: String,
        questionSummary: String,
        mistakeType: String,
        whyIMadeIt: String,
        correctIdea: String,
        source: String
    ) {
        dao.insertErrorLog(
            ErrorLogEntity(
                subject = subject,
                chapter = chapter.ifBlank { "General" },
                questionSummary = questionSummary.trim(),
                mistakeType = mistakeType,
                whyIMadeIt = whyIMadeIt.trim(),
                correctIdea = correctIdea.trim(),
                source = source,
                dateString = todayDateString(),
                reviewStage = 1,
                nextReviewLabel = "Review in 24 hours",
                isResolved = false
            )
        )
    }

    suspend fun advanceErrorLogReview(entry: ErrorLogEntity) {
        val nextStage = (entry.reviewStage + 1).coerceAtMost(4)
        val nextLabel = when (nextStage) {
            1 -> "Review in 24 hours"
            2 -> "Review in 3 days"
            3 -> "Review in 7 days"
            else -> "Mastered • Reviewed across 24h, 3d, 7d"
        }
        dao.updateErrorLog(
            entry.copy(
                reviewStage = nextStage,
                nextReviewLabel = nextLabel,
                isResolved = nextStage >= 4
            )
        )
    }

    // Projects (BUILD section)
    suspend fun updateProject(project: ProjectEntity) {
        dao.updateProject(project)
    }

    suspend fun addProject(
        name: String,
        tagline: String,
        problemStatement: String,
        targetUser: String,
        solutionSummary: String,
        technologyStack: String,
        nextConcreteAction: String
    ) {
        dao.insertProject(
            ProjectEntity(
                name = name.trim(),
                tagline = tagline.trim().ifBlank { "Student Innovation Project" },
                pipelineStage = "IDEA",
                problemStatement = problemStatement.trim(),
                targetUser = targetUser.trim(),
                solutionSummary = solutionSummary.trim(),
                prototypeDetails = "Initial concept",
                technologyStack = technologyStack.trim(),
                myRole = "Creator & Builder",
                testingSummary = "Not tested yet",
                userFeedbackSummary = "No feedback logged yet",
                impactSummary = "Documenting baseline problem",
                githubOrDemoUrl = "",
                competitionTarget = "Portfolio",
                status = "Idea Stage",
                nextConcreteAction = nextConcreteAction.trim().ifBlank { "Write problem statement and talk to 1 target user." },
                evProblemDocumented = problemStatement.isNotBlank()
            )
        )
    }

    // Profile Achievements
    suspend fun addProfileAchievement(
        category: String,
        title: String,
        dateString: String,
        organization: String,
        role: String,
        whatIDid: String,
        resultSummary: String,
        impactSummary: String,
        evidenceNote: String,
        linkUrl: String
    ) {
        dao.insertProfileAchievement(
            ProfileAchievementEntity(
                category = category,
                title = title.trim(),
                dateString = dateString.trim().ifBlank { "2026" },
                organization = organization.trim(),
                role = role.trim(),
                whatIDid = whatIDid.trim(),
                resultSummary = resultSummary.trim(),
                impactSummary = impactSummary.trim(),
                evidenceNote = evidenceNote.trim(),
                linkUrl = linkUrl.trim()
            )
        )
    }

    suspend fun deleteProfileAchievement(id: Int) {
        dao.deleteProfileAchievement(id)
    }

    // Opportunities & Scholarships
    suspend fun addOpportunity(
        name: String,
        category: String,
        country: String,
        organization: String,
        eligibility: String,
        deadline: String,
        funding: String,
        applicationRequirements: String,
        officialSource: String,
        status: String,
        notes: String,
        isVerified: Boolean
    ) {
        dao.insertOpportunity(
            OpportunityEntity(
                name = name.trim(),
                category = category,
                country = country.trim().ifBlank { "India / Global" },
                organization = organization.trim(),
                eligibility = eligibility.trim(),
                deadline = deadline.trim().ifBlank { "Unverified — check official portal" },
                funding = funding.trim().ifBlank { "Unverified" },
                applicationRequirements = applicationRequirements.trim(),
                officialSource = officialSource.trim(),
                status = status,
                notes = notes.trim(),
                isVerified = isVerified,
                lastVerifiedDate = if (isVerified) "Verified on ${todayDateString()}" else "Unverified — verify on official portal"
            )
        )
    }

    suspend fun updateOpportunity(opportunity: OpportunityEntity) {
        dao.updateOpportunity(opportunity)
    }

    suspend fun deleteOpportunity(id: Int) {
        dao.deleteOpportunity(id)
    }

    // Night Brain Dump ("EMPTY YOUR HEAD")
    suspend fun saveBrainDump(
        content: String,
        category: String // Study, Project, Scholarship, Personal, Tomorrow, Random
    ) {
        val bucket = when (category) {
            "Tomorrow" -> "Tomorrow → tasks"
            "Study", "Project" -> "Important → priority"
            "Scholarship" -> "Long-term → backlog"
            else -> "Random → notes"
        }
        val today = todayDateString()
        val convertToTask = category == "Tomorrow" || category == "Study" || category == "Project"
        dao.insertBrainDump(
            BrainDumpEntity(
                content = content.trim(),
                category = category,
                organizedBucket = bucket,
                dateString = today,
                convertedToTask = convertToTask
            )
        )
        if (convertToTask) {
            addTask(
                title = content.trim().take(65),
                subtitle = "Captured from Night Brain Dump ($category)",
                whyItMatters = "Cleared from your mind at night so you can rest peacefully.",
                domain = when (category) {
                    "Project" -> "PROJECT"
                    "Study" -> "ACADEMIC"
                    else -> "PERSONAL"
                },
                priorityBucket = if (category == "Tomorrow") "MUST DO" else "SHOULD DO",
                durationMinutes = 25,
                energyRequired = "NORMAL",
                scheduledTimeSlot = "Tomorrow"
            )
        }
    }

    // Distraction Pattern Logging
    suspend fun logFocusSessionResult(
        taskTitle: String,
        focusRating: String,
        distractionSource: String,
        durationMinutes: Int
    ) {
        dao.insertDistractionLog(
            DistractionLogEntity(
                taskTitle = taskTitle,
                focusRating = focusRating,
                distractionSource = distractionSource,
                durationMinutes = durationMinutes,
                dateString = todayDateString()
            )
        )
    }

    // Wellness Habits, Skincare, Hydration, Confidence
    suspend fun toggleHabitCompletion(habit: DailyHabitEntity) {
        val newStatus = !habit.completedToday
        val updatedSubtitle = when (habit.category) {
            "GLOW" -> if (newStatus) "Self-care complete ✨" else "Morning & evening skincare + hygiene"
            "HYDRATE" -> if (newStatus) "Hydration break complete 💧" else "Steady water breaks through school & study"
            "MOVE" -> if (newStatus) "Movement complete for today 🧘" else habit.subtitle
            "SLEEP" -> if (newStatus) "Wind-down complete 🌙" else habit.subtitle
            "CONFIDENCE" -> if (newStatus) "Checked in today 🌸" else "How did you show up today?"
            else -> habit.subtitle
        }
        dao.updateHabit(habit.copy(completedToday = newStatus, subtitle = updatedSubtitle))
        syncTodayProgress(habit.category, newStatus)
    }

    private suspend fun syncTodayProgress(category: String, completed: Boolean) {
        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        val updated = when (category) {
            "MOVE" -> current.copy(movementCompleted = completed)
            "HYDRATE" -> current.copy(
                hydrationCompletedCount = if (completed) maxOf(1, current.hydrationCompletedCount + 1) else current.hydrationCompletedCount
            )
            "GLOW" -> current.copy(morningSkincareCompleted = completed)
            "SLEEP" -> current.copy(sleepWindDownCompleted = completed)
            "CONFIDENCE" -> current.copy(confidenceCheckInCompleted = completed)
            else -> current
        }
        dao.saveDailyProgress(updated)
    }

    suspend fun addCustomHabit(title: String, subtitle: String, emoji: String) {
        val habits = dao.getAllHabitsOnce()
        val nextOrder = (habits.maxOfOrNull { it.sortOrder } ?: 7) + 1
        dao.insertHabit(
            DailyHabitEntity(
                category = "CUSTOM",
                title = title.trim(),
                subtitle = subtitle.trim().ifEmpty { "Daily self-care" },
                emoji = emoji.ifEmpty { "🌿" },
                completedToday = false,
                sortOrder = nextOrder
            )
        )
    }

    suspend fun deleteHabit(id: Int) {
        dao.deleteHabit(id)
    }

    suspend fun markMovementRoutineCompleted(routineTitle: String, durationMinutes: Int) {
        val habits = dao.getAllHabitsOnce()
        habits.find { it.category == "MOVE" }?.let { moveHabit ->
            dao.updateHabit(
                moveHabit.copy(
                    completedToday = true,
                    subtitle = "$durationMinutes min • $routineTitle complete"
                )
            )
        }
        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(movementCompleted = true))
    }

    suspend fun toggleSkincareStep(step: SkincareStepEntity) {
        val updated = step.copy(completedToday = !step.completedToday)
        dao.updateSkincareStep(updated)

        val allSteps = dao.getSkincareStepsOnce()
        val morningSteps = allSteps.filter { it.period == "MORNING" }
        val eveningSteps = allSteps.filter { it.period == "EVENING" }

        val morningDone = morningSteps.isNotEmpty() && morningSteps.count { it.completedToday } >= (morningSteps.size / 2).coerceAtLeast(1)
        val eveningDone = eveningSteps.isNotEmpty() && eveningSteps.count { it.completedToday } >= (eveningSteps.size / 2).coerceAtLeast(1)

        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(
            current.copy(
                morningSkincareCompleted = morningDone,
                eveningSkincareCompleted = eveningDone
            )
        )

        if (morningDone || eveningDone) {
            val statusText = when {
                morningDone && eveningDone -> "Morning & evening self-care complete"
                morningDone -> "Morning self-care complete"
                else -> "Evening self-care complete"
            }
            dao.updateHabitByCategory("GLOW", true, statusText)
        }
    }

    suspend fun addSkincareStep(period: String, title: String) {
        val all = dao.getSkincareStepsOnce().filter { it.period == period }
        val nextOrder = (all.maxOfOrNull { it.sortOrder } ?: 0) + 1
        dao.insertSkincareStep(
            SkincareStepEntity(
                period = period,
                title = title.trim(),
                completedToday = false,
                sortOrder = nextOrder
            )
        )
    }

    suspend fun deleteSkincareStep(id: Int) {
        dao.deleteSkincareStep(id)
    }

    suspend fun toggleHydrationReminderEnabled(reminder: HydrationReminderEntity) {
        dao.updateHydrationReminder(reminder.copy(isEnabled = !reminder.isEnabled))
    }

    suspend fun toggleHydrationReminderCompleted(reminder: HydrationReminderEntity) {
        val newDone = !reminder.completedToday
        dao.updateHydrationReminder(reminder.copy(completedToday = newDone))

        val all = dao.getHydrationRemindersOnce()
        val completedCount = all.count { it.completedToday }
        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(hydrationCompletedCount = completedCount))

        if (completedCount > 0) {
            dao.updateHabitByCategory(
                "HYDRATE",
                true,
                "$completedCount hydration break${if (completedCount > 1) "s" else ""} enjoyed today"
            )
        }
    }

    suspend fun addHydrationReminder(timeLabel: String, hour24: Int, minute: Int) {
        dao.insertHydrationReminder(
            HydrationReminderEntity(
                timeLabel = timeLabel,
                hour24 = hour24,
                minute = minute,
                isEnabled = true,
                completedToday = false
            )
        )
    }

    suspend fun deleteHydrationReminder(id: Int) {
        dao.deleteHydrationReminder(id)
    }

    suspend fun saveConfidenceEntry(
        moodEmoji: String,
        moodLabel: String,
        proudOfText: String,
        helpedFeelGoodText: String
    ) {
        val today = todayDateString()
        dao.insertConfidenceEntry(
            ConfidenceEntryEntity(
                dateString = today,
                moodEmoji = moodEmoji,
                moodLabel = moodLabel,
                proudOfText = proudOfText.trim(),
                helpedFeelGoodText = helpedFeelGoodText.trim()
            )
        )
        dao.updateHabitByCategory("CONFIDENCE", true, "Showed up today: $moodEmoji $moodLabel")
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(confidenceCheckInCompleted = true))
    }

    suspend fun markSleepWindDownCompleted() {
        val today = todayDateString()
        dao.updateHabitByCategory("SLEEP", true, "Wind-down routine complete 🌙")
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(sleepWindDownCompleted = true))
    }

    suspend fun acknowledgeMissedBedtime() {
        val today = todayDateString()
        val current = dao.getProgressForDate(today) ?: DailyProgressEntity(dateString = today)
        dao.saveDailyProgress(current.copy(missedBedtimeAcknowledged = true))
    }

    suspend fun resetTodaysHabits() {
        val prefs = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
        dao.resetAllHabitsForToday()
        dao.resetSkincareForToday()
        dao.resetHydrationForToday()
        dao.updateHabitByCategory("HYDRATE", false, "Steady water breaks through school & study")
        dao.updateHabitByCategory("MOVE", false, "${prefs.dailyTimeCommitmentMinutes} min personalized session")
        dao.updateHabitByCategory("GLOW", false, "Morning & evening skincare + hygiene")
        dao.updateHabitByCategory("SLEEP", false, "Brain dump & wind-down at ${prefs.windDownTime}")
        dao.updateHabitByCategory("CONFIDENCE", false, "How did you show up today?")
        dao.saveDailyProgress(DailyProgressEntity(dateString = todayDateString()))
    }

    suspend fun buildExportDataSummary(): String {
        val prefs = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
        val tasks = dao.getAllTasksOnce()
        val chapters = dao.getStudyChaptersOnce()
        val errors = dao.getErrorLogsOnce()
        val projects = dao.getProjectsOnce()
        val opportunities = dao.getOpportunitiesOnce()
        val achievements = dao.getProfileAchievementsOnce()
        val entries = dao.getConfidenceEntriesOnce()

        return buildString {
            appendLine("=== ASTHA OS — Personal Operating System Export ===")
            appendLine("Know what matters. Do the next right thing.")
            appendLine("Student: ${prefs.userName} (${prefs.studentClass} • ${prefs.schoolName})")
            appendLine("Subjects: ${prefs.subjectsCsv}")
            appendLine("Energy: ${prefs.currentEnergyLevel} | Schedule: School ${prefs.schoolStartTime}-${prefs.schoolEndTime}, Drona ${prefs.dronaSchedule}")
            appendLine()
            appendLine("--- Today's Tasks (${tasks.size}) ---")
            tasks.forEach { t ->
                appendLine("${if (t.isCompleted) "[x]" else "[ ]"} [${t.priorityBucket}] ${t.title} (${t.durationMinutes}m) — ${t.subtitle}")
            }
            appendLine()
            appendLine("--- Study Chapters (${chapters.size}) ---")
            chapters.forEach { c ->
                appendLine("• ${c.subject}: ${c.chapterName} [${c.understandingStatus}] Solved ${c.questionsSolved}/${c.questionsAttempted}")
            }
            appendLine()
            appendLine("--- Error Log (${errors.size}) ---")
            errors.forEach { e ->
                appendLine("• [${e.subject} - ${e.mistakeType}] ${e.questionSummary} -> ${e.correctIdea} (${e.nextReviewLabel})")
            }
            appendLine()
            appendLine("--- Projects (${projects.size}) ---")
            projects.forEach { p ->
                appendLine("• ${p.name} [${p.pipelineStage} | Evidence: ${p.evidenceStrengthLabel}] Next: ${p.nextConcreteAction}")
            }
            appendLine()
            appendLine("--- Opportunities (${opportunities.size}) & Portfolio (${achievements.size}) ---")
            opportunities.forEach { o ->
                appendLine("• ${o.name} (${o.category}) — Status: ${o.status} [${o.lastVerifiedDate}]")
            }
            appendLine()
            appendLine("--- Wellness & Confidence Reflections (${entries.size}) ---")
            entries.take(5).forEach { e ->
                appendLine("• ${e.dateString}: ${e.moodEmoji} ${e.moodLabel} — ${e.proudOfText}")
            }
        }
    }

    suspend fun deleteAllUserData() {
        dao.clearUserPreferences()
        dao.clearAllTasks()
        dao.clearTimelineBlocks()
        dao.clearStudyChapters()
        dao.clearErrorLogs()
        dao.clearProjects()
        dao.clearProfileAchievements()
        dao.clearOpportunities()
        dao.clearBrainDumps()
        dao.clearDistractionLogs()
        dao.clearDailyHabits()
        dao.clearSkincareSteps()
        dao.clearHydrationReminders()
        dao.clearConfidenceEntries()
        dao.clearProgressHistory()
        dao.clearCalendarEvents()
        dao.clearMonthlyGoals()
        dao.clearLongTermGoals()
        dao.clearRecentWins()
        dao.clearSpecialDayPlans()
        dao.clearEventPrepMilestones()
        dao.clearEndOfDayReflections()
        dao.clearSetbackRecoveries()
        dao.clearFinancialExperiments()
        dao.clearAcademicTopics()
        dao.clearScienceExhibitionScorecards()
        dao.clearScholarshipStrategies()
        dao.clearScenarioPlans()
        dao.clearProfileEvidenceLedger()
        dao.clearAngerOverwhelmPauses()
        initializeDefaultsIfNeeded()
    }


    // Calendar Events & Deadline Intelligence
    suspend fun addCalendarEvent(
        title: String,
        dateString: String,
        endDateString: String = "",
        startTime: String,
        endTime: String,
        isAllDay: Boolean = false,
        category: String,
        priority: String,
        location: String = "",
        locationOrNotes: String,
        repeatRule: String = "None",
        reminderEnabled: Boolean = true,
        reminderPreference: String = "Day before & 1 hour before",
        linkedProjectOrGoal: String = "",
        beforeEventChecklistCsv: String = ""
    ) {
        if (title.isBlank()) return
        val startD = dateString.ifBlank { todayDateString() }
        val endD = endDateString.ifBlank { startD }
        val defaultChecklist = beforeEventChecklistCsv.ifBlank {
            when (category.uppercase()) {
                "NCSC", "COMPETITIONS", "HACKATHONS" ->
                    "Finalize presentation slides & charts,Prepare 90-second opening & full script,Check project logbook & prototype,Practice judge Q&A aloud,Confirm travel & timing"
                "EXAMS / TESTS", "ACADEMICS", "REVISION" ->
                    "Identify syllabus & key topics,Review formula sheet & weak concepts,Solve 4 mixed practice questions,Re-solve logged mistakes,Sleep on time"
                "PROJECTS", "RESEARCH" ->
                    "Verify prototype demo works smoothly,Prepare 3 user test questions,Record observations & evidence"
                "SCHOLARSHIPS", "APPLICATIONS" ->
                    "Verify official portal eligibility & deadline,Gather verified project evidence,Draft & refine essay responses"
                else ->
                    "Confirm timing & location,Prepare needed materials"
            }
        }
        dao.insertCalendarEvent(
            CalendarEventEntity(
                title = title.trim(),
                dateString = startD,
                endDateString = endD,
                startTime = if (isAllDay) "All Day" else startTime.ifBlank { "9:00 AM" },
                endTime = if (isAllDay) "All Day" else endTime.ifBlank { "10:00 AM" },
                isAllDay = isAllDay,
                category = category,
                priority = priority,
                location = location.trim(),
                locationOrNotes = locationOrNotes.trim(),
                repeatRule = repeatRule,
                reminderEnabled = reminderEnabled,
                reminderPreference = reminderPreference,
                linkedProjectOrGoal = linkedProjectOrGoal.trim(),
                beforeEventChecklistCsv = defaultChecklist
            )
        )
        // Auto-generate non-duplicating preparation milestones for high-priority events
        val insertedEvents = dao.getCalendarEventsOnce()
        val newest = insertedEvents.maxByOrNull { it.id }
        if (newest != null && newest.title.equals(title.trim(), ignoreCase = true)) {
            generateEventPrepMilestonesIfMissing(newest)
        }
    }

    private suspend fun generateEventPrepMilestonesIfMissing(event: CalendarEventEntity) {
        val existing = dao.getEventPrepMilestonesOnce().filter { it.eventId == event.id }
        if (existing.isNotEmpty()) return
        val cat = event.category.uppercase()
        val steps = when {
            cat in listOf("NCSC", "COMPETITIONS", "HACKATHONS") -> listOf(
                Triple("ev_${event.id}_slides", "BEFORE", "Finalize presentation slides & data charts for ${event.title}"),
                Triple("ev_${event.id}_script", "BEFORE", "Rehearse 90-second opening & judge Q&A for ${event.title}"),
                Triple("ev_${event.id}_pack", "BEFORE", "Check logbook, prototype & pack materials for ${event.title}"),
                Triple("ev_${event.id}_after", "AFTER", "Save ${event.title} reflection & evidence to Profile")
            )
            cat in listOf("EXAMS / TESTS", "ACADEMICS") -> listOf(
                Triple("ev_${event.id}_syllabus", "BEFORE", "Review formula sheet & weak concepts for ${event.title}"),
                Triple("ev_${event.id}_practice", "BEFORE", "Solve 4 mixed practice questions & review Error Log for ${event.title}"),
                Triple("ev_${event.id}_after_err", "AFTER", "Log any tricky questions from ${event.title} into Error Log")
            )
            cat in listOf("SCHOLARSHIPS", "APPLICATIONS") -> listOf(
                Triple("ev_${event.id}_reqs", "BEFORE", "Check requirements & gather verified evidence for ${event.title}"),
                Triple("ev_${event.id}_draft", "BEFORE", "Draft & refine responses for ${event.title} ahead of deadline")
            )
            else -> emptyList()
        }
        if (steps.isNotEmpty()) {
            dao.insertEventPrepMilestones(
                steps.map { (key, phase, stepTitle) ->
                    EventPrepMilestoneEntity(
                        eventId = event.id,
                        eventTitle = event.title,
                        milestoneKey = key,
                        phase = phase,
                        stepTitle = stepTitle,
                        stepInstruction = event.locationOrNotes.ifBlank { "Calm 20-minute preparation step for ${event.title}" },
                        targetDateString = event.dateString,
                        durationMinutes = 20,
                        status = "SUGGESTED"
                    )
                }
            )
        }
    }

    // Special Day Planner ("MY DAY, MY WAY")
    suspend fun saveSpecialDayPlan(
        dateString: String,
        dayType: String,
        customFocusNote: String,
        customStructureItemsCsv: String,
        conflictOverrideApproved: Boolean = false
    ) {
        val template = DefaultWellnessData.dayTypeTemplates.find { it.typeName == dayType }
        val effectiveStructure = customStructureItemsCsv.ifBlank {
            template?.defaultStructure?.joinToString(",") ?: "1 main priority,1 wellness reset"
        }
        dao.saveSpecialDayPlan(
            SpecialDayPlanEntity(
                dateString = dateString.ifBlank { todayDateString() },
                dayType = dayType,
                customFocusNote = customFocusNote.trim().ifBlank { template?.tagline ?: "" },
                customStructureItemsCsv = effectiveStructure,
                conflictOverrideApproved = conflictOverrideApproved
            )
        )
    }

    suspend fun deleteSpecialDayPlan(dateString: String) {
        dao.deleteSpecialDayPlan(dateString)
    }

    // Event Prep Milestones (Idempotent — never duplicates tasks)
    suspend fun handleEventPrepMilestoneAction(
        milestone: EventPrepMilestoneEntity,
        action: String // ACCEPT, POSTPONE, EDIT_AND_ACCEPT, DISMISS
    ) {
        when (action) {
            "ACCEPT" -> {
                dao.updateEventPrepMilestone(milestone.copy(status = "ACCEPTED"))
                val allTasks = dao.getAllTasksOnce()
                val alreadyExists = allTasks.any {
                    (it.prepMilestoneKey.isNotBlank() && it.prepMilestoneKey == milestone.milestoneKey) ||
                        (it.title.equals(milestone.stepTitle, ignoreCase = true) && !it.isCompleted)
                }
                if (!alreadyExists) {
                    dao.insertTask(
                        OsTaskEntity(
                            title = milestone.stepTitle,
                            subtitle = milestone.stepInstruction,
                            whyItMatters = "Advance preparation for ${milestone.eventTitle}",
                            domain = "COMPETITION",
                            priorityBucket = "MUST DO",
                            durationMinutes = milestone.durationMinutes,
                            energyRequired = "NORMAL",
                            scheduledTimeSlot = "Today",
                            dateString = todayDateString(),
                            linkedEventId = milestone.eventId,
                            prepMilestoneKey = milestone.milestoneKey
                        )
                    )
                }
            }
            "POSTPONE" -> {
                dao.updateEventPrepMilestone(milestone.copy(status = "POSTPONED"))
            }
            "DISMISS" -> {
                dao.updateEventPrepMilestone(milestone.copy(status = "DISMISSED"))
            }
            "COMPLETE" -> {
                dao.updateEventPrepMilestone(milestone.copy(status = "COMPLETED"))
                recordRecentWin(
                    title = milestone.stepTitle.take(32),
                    whatChangedDetail = "${milestone.eventTitle}: Completed prep step (${milestone.stepTitle})",
                    category = "BUILD"
                )
            }
        }
    }

    // End-of-Day Reflection & Unfinished Task Handling
    suspend fun saveEndOfDayReflection(
        dateString: String,
        meaningfulToday: String,
        learnedOrExperienced: String,
        gladMadeTimeFor: String,
        carryIntoTomorrow: String,
        letGoOfForNow: String,
        quickTagSelected: String,
        tomorrowFirstStep: String
    ) {
        val targetDate = dateString.ifBlank { todayDateString() }
        dao.saveEndOfDayReflection(
            EndOfDayReflectionEntity(
                dateString = targetDate,
                meaningfulToday = meaningfulToday.trim(),
                learnedOrExperienced = learnedOrExperienced.trim(),
                gladMadeTimeFor = gladMadeTimeFor.trim(),
                carryIntoTomorrow = carryIntoTomorrow.trim(),
                letGoOfForNow = letGoOfForNow.trim(),
                quickTagSelected = quickTagSelected.trim(),
                tomorrowFirstStep = tomorrowFirstStep.trim(),
                dayClosed = true
            )
        )
        if (tomorrowFirstStep.isNotBlank()) {
            addTask(
                title = tomorrowFirstStep.trim(),
                subtitle = "Set during last night's Close Today Gently reflection",
                whyItMatters = "Gives you a clear, calm starting point for tomorrow.",
                domain = "ACADEMIC",
                priorityBucket = "MUST DO",
                durationMinutes = 25,
                energyRequired = "NORMAL",
                scheduledTimeSlot = "First Focus Block"
            )
        }
        val detail = quickTagSelected.ifBlank { meaningfulToday.ifBlank { "Closed the day mindfully" } }
        recordRecentWin(
            title = "Closed Day Gently",
            whatChangedDetail = "Reflection: $detail",
            category = "WELLNESS"
        )
    }

    suspend fun handleUnfinishedTaskEndOfDay(
        task: OsTaskEntity,
        decision: String // RESCHEDULE, KEEP IN BACKLOG, DROP, DECIDE TOMORROW
    ) {
        when (decision) {
            "RESCHEDULE" -> {
                dao.updateTask(
                    task.copy(
                        isPostponed = true,
                        endOfDayDecision = "RESCHEDULE",
                        scheduledTimeSlot = "Tomorrow"
                    )
                )
            }
            "KEEP IN BACKLOG" -> {
                dao.updateTask(
                    task.copy(
                        priorityBucket = "CAN POSTPONE",
                        isTopThree = false,
                        endOfDayDecision = "KEEP IN BACKLOG"
                    )
                )
            }
            "DROP" -> {
                dao.deleteTask(task.id)
            }
            "DECIDE TOMORROW" -> {
                dao.updateTask(
                    task.copy(
                        isPostponed = true,
                        endOfDayDecision = "DECIDE TOMORROW"
                    )
                )
            }
        }
    }

    suspend fun updateCalendarEvent(event: CalendarEventEntity) {
        dao.updateCalendarEvent(event)
    }

    suspend fun deleteCalendarEvent(id: Int) {
        dao.deleteCalendarEvent(id)
    }

    suspend fun acceptDeadlineSuggestion(suggestion: DeadlinePreparationSuggestion) {
        addTask(
            title = suggestion.suggestedTaskTitle,
            subtitle = suggestion.suggestedTaskSubtitle,
            whyItMatters = "${suggestion.eventTitle} (${suggestion.countdownCalmText})",
            domain = when (suggestion.category) {
                "COMPETITION" -> "COMPETITION"
                "PROJECT" -> "PROJECT"
                "SCHOLARSHIP", "APPLICATION" -> "SCHOLARSHIP"
                else -> "ACADEMIC"
            },
            priorityBucket = "MUST DO",
            durationMinutes = suggestion.durationMinutes,
            energyRequired = "NORMAL",
            scheduledTimeSlot = "Today"
        )
        val events = dao.getCalendarEventsOnce()
        events.find { it.id == suggestion.eventId }?.let { ev ->
            val nextDismissed = (ev.dismissedMilestoneDaysCsv.split(",") + suggestion.daysRemaining.toString())
                .filter { it.isNotBlank() }.distinct().joinToString(",")
            dao.updateCalendarEvent(
                ev.copy(
                    preparationStarted = true,
                    preparationCompletedCount = ev.preparationCompletedCount + 1,
                    dismissedMilestoneDaysCsv = nextDismissed
                )
            )
        }
    }

    suspend fun dismissDeadlineSuggestion(suggestion: DeadlinePreparationSuggestion) {
        val events = dao.getCalendarEventsOnce()
        events.find { it.id == suggestion.eventId }?.let { ev ->
            val nextDismissed = (ev.dismissedMilestoneDaysCsv.split(",") + suggestion.daysRemaining.toString())
                .filter { it.isNotBlank() }.distinct().joinToString(",")
            dao.updateCalendarEvent(ev.copy(dismissedMilestoneDaysCsv = nextDismissed))
        }
    }

    // Monthly Goals & Long-Term Goals
    suspend fun addMonthlyGoal(
        domain: String,
        title: String,
        weeklyTarget: String,
        todaySmallestStep: String,
        linkedLongTermArea: String = domain
    ) {
        if (title.isBlank()) return
        dao.insertMonthlyGoal(
            MonthlyGoalEntity(
                monthLabel = "October 2026",
                domain = domain,
                title = title.trim(),
                weeklyTarget = weeklyTarget.trim().ifBlank { "1 focused milestone per week" },
                todaySmallestStep = todaySmallestStep.trim().ifBlank { "Complete 1 20-minute action today" },
                linkedLongTermArea = linkedLongTermArea,
                progressStepsDone = 0,
                progressStepsTotal = 4
            )
        )
    }

    suspend fun advanceMonthlyGoalStep(goal: MonthlyGoalEntity) {
        val nextDone = (goal.progressStepsDone + 1).coerceAtMost(goal.progressStepsTotal)
        val isNowDone = nextDone >= goal.progressStepsTotal
        dao.updateMonthlyGoal(
            goal.copy(
                progressStepsDone = nextDone,
                isCompleted = isNowDone
            )
        )
        dao.insertRecentWin(
            RecentWinEntity(
                title = goal.title.take(34),
                whatChangedDetail = "${goal.domain}: Monthly goal progressed ($nextDone/${goal.progressStepsTotal})",
                category = "GOAL",
                dateString = todayDateString()
            )
        )
    }

    suspend fun updateMonthlyGoal(goal: MonthlyGoalEntity) {
        dao.updateMonthlyGoal(goal)
    }

    suspend fun deleteMonthlyGoal(id: Int) {
        dao.deleteMonthlyGoal(id)
    }

    suspend fun updateLongTermGoal(goal: LongTermGoalEntity) {
        dao.updateLongTermGoal(goal)
    }

    suspend fun recordRecentWin(title: String, whatChangedDetail: String, category: String) {
        dao.insertRecentWin(
            RecentWinEntity(
                title = title,
                whatChangedDetail = whatChangedDetail,
                category = category,
                dateString = todayDateString()
            )
        )
    }

    // Setback Recovery ("Learn Without Losing Self-Respect")
    suspend fun addSetbackRecovery(
        eventTitle: String,
        category: String,
        whatActuallyHappened: String,
        howAmIFeeling: String,
        whatIsStillTrueAboutMe: String,
        whatWasOutOfMyControl: String,
        whatWasInMyControl: String,
        actionableLesson: String,
        gentleNextStep: String
    ) {
        val entry = SetbackRecoveryEntity(
            dateString = todayDateString(),
            eventTitle = eventTitle,
            category = category,
            whatActuallyHappened = whatActuallyHappened,
            howAmIFeeling = howAmIFeeling,
            whatIsStillTrueAboutMe = whatIsStillTrueAboutMe,
            whatWasOutOfMyControl = whatWasOutOfMyControl,
            whatWasInMyControl = whatWasInMyControl,
            actionableLesson = actionableLesson,
            gentleNextStep = gentleNextStep,
            isResolved = true
        )
        dao.insertSetbackRecovery(entry)
        dao.insertRecentWin(
            RecentWinEntity(
                title = "Processed setback calmly",
                whatChangedDetail = "Self-Respect: Analyzed '$eventTitle' without self-blame",
                category = "MINDSET",
                dateString = todayDateString()
            )
        )
    }

    suspend fun updateSetbackRecovery(entry: SetbackRecoveryEntity) {
        dao.updateSetbackRecovery(entry)
    }

    suspend fun deleteSetbackRecovery(id: Int) {
        dao.deleteSetbackRecovery(id)
    }

    // Financial Experiments & Student Value Creation
    suspend fun addFinancialExperiment(
        title: String,
        skillUsed: String,
        experimentStage: String,
        estimatedEarningPotential: String,
        actualEarned: Double,
        clientOrPlatform: String,
        whatLearnedAboutValue: String
    ) {
        dao.insertFinancialExperiment(
            FinancialExperimentEntity(
                title = title,
                skillUsed = skillUsed,
                experimentStage = experimentStage,
                estimatedEarningPotential = estimatedEarningPotential,
                actualEarned = actualEarned,
                clientOrPlatform = clientOrPlatform,
                whatLearnedAboutValue = whatLearnedAboutValue,
                dateString = todayDateString()
            )
        )
        dao.insertRecentWin(
            RecentWinEntity(
                title = "Value experiment: $title",
                whatChangedDetail = "Skills to Income: Logged $title ($skillUsed)",
                category = "SKILLS",
                dateString = todayDateString()
            )
        )
    }

    suspend fun updateFinancialExperiment(entry: FinancialExperimentEntity) {
        dao.updateFinancialExperiment(entry)
    }

    suspend fun deleteFinancialExperiment(id: Int) {
        dao.deleteFinancialExperiment(id)
    }

    // 1. Academic Topics & Chemistry Mission
    suspend fun addAcademicTopic(topic: AcademicTopicEntity) {
        dao.insertAcademicTopic(topic)
    }

    suspend fun updateAcademicTopic(topic: AcademicTopicEntity) {
        dao.updateAcademicTopic(topic)
    }

    suspend fun deleteAcademicTopic(id: Int) {
        dao.deleteAcademicTopic(id)
    }

    // 2. Science Exhibition Scorecards
    suspend fun addScienceExhibitionScorecard(card: ScienceExhibitionScorecardEntity) {
        dao.insertScienceExhibitionScorecard(card)
    }

    suspend fun updateScienceExhibitionScorecard(card: ScienceExhibitionScorecardEntity) {
        dao.updateScienceExhibitionScorecard(card)
    }

    suspend fun deleteScienceExhibitionScorecard(id: Int) {
        dao.deleteScienceExhibitionScorecard(id)
    }

    suspend fun selectProjectForExecution(id: Int) {
        val all = dao.getScienceExhibitionScorecardsOnce()
        all.forEach { card ->
            dao.updateScienceExhibitionScorecard(card.copy(isSelectedForExecution = (card.id == id)))
        }
    }

    suspend fun advanceExecutionMilestone(id: Int) {
        val all = dao.getScienceExhibitionScorecardsOnce()
        val found = all.find { it.id == id } ?: return
        val nextMilestone = if (found.executionMilestone < 10) found.executionMilestone + 1 else 10
        dao.updateScienceExhibitionScorecard(found.copy(executionMilestone = nextMilestone))
    }

    // 3. Scholarship Strategies
    suspend fun addScholarshipStrategy(strategy: ScholarshipStrategyEntity) {
        dao.insertScholarshipStrategy(strategy)
    }

    suspend fun updateScholarshipStrategy(strategy: ScholarshipStrategyEntity) {
        dao.updateScholarshipStrategy(strategy)
    }

    suspend fun deleteScholarshipStrategy(id: Int) {
        dao.deleteScholarshipStrategy(id)
    }

    // 4. Scenario Plans
    suspend fun addScenarioPlan(plan: ScenarioPlanEntity) {
        dao.insertScenarioPlan(plan)
    }

    suspend fun updateScenarioPlan(plan: ScenarioPlanEntity) {
        dao.updateScenarioPlan(plan)
    }

    suspend fun deleteScenarioPlan(id: Int) {
        dao.deleteScenarioPlan(id)
    }

    // 5. Profile Evidence Ledger
    suspend fun addProfileEvidence(item: ProfileEvidenceLedgerEntity) {
        dao.insertProfileEvidence(item)
    }

    suspend fun updateProfileEvidence(item: ProfileEvidenceLedgerEntity) {
        dao.updateProfileEvidence(item)
    }

    suspend fun deleteProfileEvidence(id: Int) {
        dao.deleteProfileEvidence(id)
    }

    // 6. Anger & Overwhelm Pauses
    suspend fun addAngerOverwhelmPause(
        trigger: String,
        impulse: String,
        reality: String,
        alt1: String,
        alt2: String,
        response: String
    ) {
        dao.insertAngerOverwhelmPause(
            AngerOverwhelmPauseEntity(
                triggerEvent = trigger,
                firstImpulseOrEmotion = impulse,
                factualRealityCheck = reality,
                alternativeInterpretation1 = alt1,
                alternativeInterpretation2 = alt2,
                chosenConstructiveResponse = response,
                dateString = todayDateString()
            )
        )
    }

    suspend fun deleteAngerOverwhelmPause(id: Int) {
        dao.deleteAngerOverwhelmPause(id)
    }

    // 7. THE MORNING DECISION ENGINE
    suspend fun generateMorningDecisionPlan(
        availableMinutesOverride: Int? = null,
        energyOverride: String? = null
    ): MorningDecisionPlan {
        val todayStr = todayDateString()
        val prefs = dao.getUserPreferencesOnce() ?: UserPreferencesEntity()
        val availableMins = availableMinutesOverride ?: prefs.dailyTimeCommitmentMinutes
        val energy = energyOverride ?: prefs.currentEnergyLevel
        val topics = dao.getAcademicTopicsOnce()
        val calendarEvents = dao.getCalendarEventsOnce()
        val errorLogs = dao.getErrorLogsOnce()
        val tasks = dao.getAllTasksOnce().filter { !it.isCompleted && !it.isPostponed }

        // Find upcoming exam in next 7 days
        val upcomingExam = calendarEvents.firstOrNull {
            it.category in listOf("EXAMS / TESTS", "ACADEMICS", "COMPETITIONS") &&
                    it.dateString >= todayStr
        }

        // Identify starting topic
        val chemFocus = topics.firstOrNull { it.isChemistryFullMarksFocus && it.practiceLevel != "MASTERED" }
        val weakTopic = topics.firstOrNull { it.requiresTeacherGuidance || it.practiceLevel in listOf("NOT_STARTED", "LESSON_WATCHED") }
        val primaryTopic = chemFocus ?: weakTopic ?: topics.firstOrNull() ?: AcademicTopicEntity(
            subject = "Physics",
            chapterName = "Rotational Motion",
            topicName = "Torque and Angular Momentum",
            syllabusType = "CURRENT"
        )

        val p1Duration = when {
            availableMins <= 45 -> 25
            availableMins <= 90 -> 35
            else -> 45
        }

        val priority1 = MorningStudyPriority(
            priorityRank = 1,
            subject = primaryTopic.subject,
            exactTopic = "${primaryTopic.chapterName}: ${primaryTopic.topicName}",
            startingResourceOrMethod = when {
                primaryTopic.isChemistryFullMarksFocus -> "NCERT Theory & Section Notes: Read core laws and written definitions first (no video)."
                primaryTopic.practiceLevel == "NOT_STARTED" -> "Standard textbook / coaching module summary note (10 min read)."
                else -> "Formula flashcards + 1 worked example problem."
            },
            activeRecallStep = "Close notes. Write key formulas, sign conventions, and assumptions on blank paper from memory.",
            questionsToAttempt = when (primaryTopic.practiceLevel) {
                "NOT_STARTED", "LESSON_WATCHED" -> "Solve 3 fundamental School-Level NCERT in-text questions."
                "SCHOOL_LEVEL" -> "Solve 4 standard coaching / NCERT Exemplar questions."
                else -> "Solve 3 multi-step exam-level timed problems."
            },
            completionCriterion = "All 3-4 problems solved independently with complete steps, correct units, and diagrams without peeking at answers.",
            nextStepIfStuck = "Consult the 5-Level I'm Stuck system: Level 1 (re-read question), Level 2 (draw diagram), Level 3 (check Error Log), Level 4 (ASAA AI hint).",
            estimatedMinutes = p1Duration
        )

        // Priority 2: Secondary academic recall or project milestone
        val p2Duration = when {
            availableMins <= 45 -> 15
            availableMins <= 90 -> 25
            else -> 35
        }

        val secondaryTopic = topics.firstOrNull { it.id != primaryTopic.id } ?: AcademicTopicEntity(
            subject = "Mathematics",
            chapterName = "Trigonometric Functions",
            topicName = "Multiple Angle Identities",
            syllabusType = "CURRENT"
        )

        val priority2 = MorningStudyPriority(
            priorityRank = 2,
            subject = secondaryTopic.subject,
            exactTopic = "${secondaryTopic.chapterName}: ${secondaryTopic.topicName}",
            startingResourceOrMethod = "Error Log review: Identify 2 previous mistakes from this chapter.",
            activeRecallStep = "Re-solve the 2 missed error-log problems untimed on scratch paper.",
            questionsToAttempt = "Attempt 3 medium-level numericals or algebraic proofs.",
            completionCriterion = "Zero repeat of the previous mistake pattern in calculation or formula choice.",
            nextStepIfStuck = "Write down the exact derivation line where confusion occurs for teacher query.",
            estimatedMinutes = p2Duration
        )

        // Priority 3: Project / Future Readiness / Wellness Reset
        val p3Duration = when {
            availableMins <= 45 -> 10
            availableMins <= 90 -> 20
            else -> 25
        }

        val priority3 = MorningStudyPriority(
            priorityRank = 3,
            subject = "Project & Evidence",
            exactTopic = "Krow or Smart EcoDrain: Document 1 tangible evidence item",
            startingResourceOrMethod = "Open Project Evidence Ledger or GitHub repository.",
            activeRecallStep = "Summarize the problem statement in 2 clear sentences without technical jargon.",
            questionsToAttempt = "Test 1 prototype feature or draft 1 judge Q&A answer.",
            completionCriterion = "1 concrete observation or code commit saved in the evidence ledger.",
            nextStepIfStuck = "Focus on a 5-minute sketch or list 3 user assumptions to test tomorrow.",
            estimatedMinutes = p3Duration
        )

        val whatCanWait = when {
            upcomingExam != null -> "Optional coding tutorials, secondary scholarship searches, and non-urgent YouTube lectures can wait until after the exam."
            availableMins < 60 -> "Long video lectures, full chapter rewrites, and deep project feature coding can wait for the weekend."
            else -> "Unverified scholarship applications, entertainment browsing, and secondary subject reorganizations can wait."
        }

        return MorningDecisionPlan(
            mostImportantThingToday = when {
                upcomingExam != null -> "Upcoming ${upcomingExam.title} (${upcomingExam.dateString}): Master ${primaryTopic.topicName} with zero formula doubts."
                primaryTopic.isChemistryFullMarksFocus -> "Chemistry Full-Marks Mission: ${primaryTopic.topicName} derivations and numerical practice."
                else -> "Solid foundation in ${primaryTopic.subject}: ${primaryTopic.topicName} with independent question recall."
            },
            startingSubject = primaryTopic.subject,
            startingExactTopic = "${primaryTopic.chapterName} — ${primaryTopic.topicName}",
            whatToDoAfterStudying = "Take a 5-minute posture & hydration reset, then spend 15 minutes reviewing 2 past mistakes in the Error Log.",
            nextPriorityAfterThat = "${secondaryTopic.subject} practice, followed by 20 minutes of project evidence / NCSC prep.",
            whatCanWaitUntilAnotherDay = whatCanWait,
            priorities = listOf(priority1, priority2, priority3),
            availableMinutes = availableMins,
            energyLevel = energy,
            generatedDateString = todayStr
        )
    }
}



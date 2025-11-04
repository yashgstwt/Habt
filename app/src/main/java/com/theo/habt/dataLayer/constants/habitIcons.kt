package com.theo.habt.dataLayer.constants

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bedtime

import androidx.compose.material.icons.filled.Brush

import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NoCell
import androidx.compose.material.icons.filled.NoFood
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SmokeFree
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Yard
import androidx.compose.ui.graphics.vector.ImageVector

val INITAL_ICON_ID = "fitness_gym"
val habitIcons: Map<String, ImageVector> = mapOf(
    // --- General & Goals ---
    "goal_star" to Icons.Default.Star,
    "general_check_circle" to Icons.Default.CheckCircle,
    "goal_flag" to Icons.Default.Flag,
    "general_trending_up" to Icons.Default.TrendingUp,
    "goal_premium" to Icons.Default.WorkspacePremium,
    "general_thumb_up" to Icons.Default.ThumbUp,
    "health_favorite" to Icons.Default.Favorite,
    "general_repeat" to Icons.Default.Repeat,
    "general_task_alt" to Icons.Default.TaskAlt,

    // --- Health & Fitness ---
    "fitness_gym" to Icons.Default.FitnessCenter,
    "fitness_run" to Icons.Default.DirectionsRun,
    "health_meditation" to Icons.Default.SelfImprovement,
    "health_heart" to Icons.Default.MonitorHeart,
    "health_water_drop" to Icons.Default.WaterDrop,
    "food_restaurant" to Icons.Default.Restaurant,
    "food_no_food" to Icons.Default.NoFood,
    "health_bedtime" to Icons.Default.Bedtime,
    "health_spa" to Icons.Default.Spa,
    "fitness_gymnastics" to Icons.Default.SportsGymnastics,
    "fitness_pool" to Icons.Default.Pool,

    // --- Productivity & Learning ---
    "learning_book" to Icons.Default.MenuBook,
    "learning_school" to Icons.Default.School,
    "productivity_work" to Icons.Default.Work,
    "productivity_idea" to Icons.Default.Lightbulb,
    "productivity_edit" to Icons.Default.Edit,
    "productivity_calendar" to Icons.Default.CalendarMonth,
    "tech_computer" to Icons.Default.Computer,
    "tech_code" to Icons.Default.Code,
    "finance_bar_chart" to Icons.Default.BarChart,
    "finance_savings" to Icons.Default.Savings,

    // --- Hobbies & Leisure ---
    "hobby_music" to Icons.Default.MusicNote,
    "hobby_brush" to Icons.Default.Brush,
    "hobby_palette" to Icons.Default.Palette,
    "hobby_camera" to Icons.Default.PhotoCamera,
    "hobby_gaming" to Icons.Default.SportsEsports,
    "leisure_theaters" to Icons.Default.Theaters,
    "leisure_nature" to Icons.Default.Forest,
    "hobby_gardening" to Icons.Default.Yard,
    "hobby_pets" to Icons.Default.Pets,
    "food_cooking" to Icons.Default.SoupKitchen,
    "leisure_travel" to Icons.Default.TravelExplore,

    // --- Home & Chores ---
    "chores_cleaning" to Icons.Default.CleaningServices,
    "chores_shopping" to Icons.Default.ShoppingCart,
    "chores_home" to Icons.Default.Home,
    "social_family" to Icons.Default.FamilyRestroom,

    // --- Communication & Social ---
    "social_groups" to Icons.Default.Groups,
    "social_call" to Icons.Default.Call,
    "social_waving_hand" to Icons.Default.WavingHand,

    // --- Habits to Break ---
    "break_smoke_free" to Icons.Default.SmokeFree,
    "break_no_cell" to Icons.Default.NoCell,
    "break_fast_food" to Icons.Default.Fastfood,
    "break_no_alcohol" to Icons.Default.LocalBar
)


//
//val habitIcons = listOf(
//    // --- General & Goals ---
//    Icons.Default.Star,               // Goal, Achievement
//    Icons.Default.CheckCircle,        // Completion, Done
//    Icons.Default.Flag,               // Milestone, Goal
//    Icons.Default.TrendingUp,         // Improvement, Progress
//    Icons.Default.WorkspacePremium,   // Excellence, Quality
//    Icons.Default.ThumbUp,            // Positive Reinforcement
//    Icons.Default.Favorite,           // Self-care, Love
//    Icons.Default.Repeat,             // Consistency, Repetition
//    Icons.Default.TaskAlt,            // Task Completed
//
//    // --- Health & Fitness ---
//    Icons.Default.FitnessCenter,      // Workout, Gym
//    Icons.Default.DirectionsRun,      // Running, Cardio
//    Icons.Default.SelfImprovement,    // Meditation, Yoga
//    Icons.Default.MonitorHeart,       // Health Tracking, Vitals
//    Icons.Default.WaterDrop,          // Drink Water
//    Icons.Default.Restaurant,         // Healthy Eating, Diet
//    Icons.Default.NoFood,             // Fasting, Quit Junk Food
//    Icons.Default.Bedtime,            // Sleep, Rest
//    Icons.Default.Spa,                // Relaxation, Self-care
//    Icons.Default.SportsGymnastics,   // Flexibility, Stretching
//    Icons.Default.Pool,               // Swimming
//
//    // --- Productivity & Learning ---
//    Icons.Default.MenuBook,           // Reading
//    Icons.Default.School,             // Studying, Learning
//    Icons.Default.Work,               // Work, Focus Time
//    Icons.Default.Lightbulb,          // Ideas, Creativity
//    Icons.Default.Edit,               // Journaling, Writing
//    Icons.Default.CalendarMonth,      // Planning, Scheduling
//    Icons.Default.Computer,           // Coding, Screen Time
//    Icons.Default.Code,               // Programming
//    Icons.Default.BarChart,           // Tracking Finances/Progress
//    Icons.Default.Savings,            // Saving Money
//
//    // --- Hobbies & Leisure ---
//    Icons.Default.MusicNote,          // Play Instrument, Listen to Music
//    Icons.Default.Brush,              // Art, Painting, Drawing
//    Icons.Default.Palette,            // Creative Hobbies
//    Icons.Default.PhotoCamera,        // Photography
//    Icons.Default.SportsEsports,      // Gaming
//    Icons.Default.Theaters,           // Watch a Movie/Show
//    Icons.Default.Forest,             // Spend time in Nature, Hiking
//    Icons.Default.Yard,               // Gardening
//    Icons.Default.Pets,               // Pet Care, Walk the Dog
//    Icons.Default.SoupKitchen,        // Cooking
//    Icons.Default.TravelExplore,      // Travel
//
//    // --- Home & Chores ---
//    Icons.Default.CleaningServices,   // Cleaning, Tidying up
//    Icons.Default.ShoppingCart,       // Groceries, Shopping
//    Icons.Default.Home,               // Home-related tasks
//    Icons.Default.FamilyRestroom,     // Family Time
//
//    // --- Communication & Social ---
//    Icons.Default.Groups,             // Socialize, Meet Friends
//    Icons.Default.Call,               // Call Family/Friends
//    Icons.Default.WavingHand,         // Be kind, Greet people
//
//    // --- Habits to Break ---
//    Icons.Default.SmokeFree,          // Quit Smoking
//    Icons.Default.NoCell,             // Reduce Screen Time
//    Icons.Default.Fastfood,           // Can be used for "Avoid Junk Food"
//    Icons.Default.LocalBar            // Reduce/Quit Alcohol
//
//
//
//)
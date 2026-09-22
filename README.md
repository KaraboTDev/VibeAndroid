
Github link: https://github.com/KaraboTDev/VibeAndroid
Video link: https://youtube.com/shorts/vjIOpHi1QUQ?si=dhY3D6mObk99BTJq
Vibe — Mood & Purpose-Based Venue Discovery

Find your space, not just a place. Vibe helps you discover cafes, coworking spaces, and study spots based on how you want to feel or what you need to do — Quiet, Social, Focus, Creative, Cheap Wifi — instead of forcing you to search by category or keyword. 

1. Purpose

Existing "vibe-based" discovery apps (Vibemap, VibeCheck, Inside Vibe) focus almost entirely on nightlife and social occasions. There's a clear gap for everyday, purpose-driven discovery — finding a quiet café to work from, a spot with reliable wifi, or somewhere social to meet friends. Vibe fills that gap by making mood/purpose the primary way you search, not an afterthought.

This repository contains the Android client (Kotlin, XML layouts) built against a custom REST API.

2. Key features

Feature	Description
Mood-based search: The home screen leads with mood chips (Quiet, Social, Focus, Creative, Cheap Wifi) instead of a category list or keyword search bar.

"Why this matches":	Every venue detail screen shows which tags contributed to the match and how many users confirmed each one — the recommendation is never a black box.

Pluggable venue data layer:	Venue search is wrapped behind an IVenueDataProvider interface on the API side, so the underlying data source (currently BizData, an OpenStreetMap-based API) can be swapped for a paid provider like Geoapify or Google Places later without touching the rest of the system.

Secure authentication: Passwords are hashed server-side with ASP.NET Core Identity (PBKDF2) — never stored or transmitted in plain text. Sessions use JWT bearer tokens, stored on-device via EncryptedSharedPreferences.

Multi-language support:	English, isiZulu, and Afrikaans — covering UI strings, mood tag names, and settings labels.
Push notifications	Firebase Cloud Messaging alerts users when a new venue matching their saved mood appears nearby.

3. Screens

Login / Register — email + password auth, Google SSO (stretch)
Home / Mood Picker — the main hub, reachable any time via bottom nav
Results List — venues matching the selected mood, sorted by distance
Venue Detail — full venue info, hours, and the "why this matches" breakdown
Settings — language, default search area, notifications, account, logout

4. Tech stack

Client: Kotlin, Android XML layouts, ViewBinding, Retrofit + OkHttp, Room, Jetpack Security (EncryptedSharedPreferences), Coroutines
API: ASP.NET Core (.NET), Entity Framework Core (Code First)
Database: Postgres
Host:Render
CI/CD: GitHub Actions

5. API endpoints
Method	Endpoint	Purpose
POST	/auth/register	Create an account (email + password)
POST	/auth/login	Authenticate, returns a JWT
POST	/auth/google	Google SSO login (stretch feature)
GET	/venues?mood={tag}&lat={lat}&lng={lng}	Venues matching a mood, merging BizData with app tag data
GET	/venues/{id}	Full detail for one venue, including tag breakdown
POST /favorites · GET /favorites · DELETE /favorites/{id}	Favorite management — the endpoint the offline sync queue targets	
PUT	/settings	Updates language, default area, notification preferences
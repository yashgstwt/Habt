package com.theo.habt.dataLayer.repositorys

sealed interface RepositoryError {

    sealed class RoomErrors :  Throwable() ,RepositoryError  {
        data object NameAlreadyExist : RoomErrors() {
            private fun readResolve(): Any = NameAlreadyExist
        }

        data object HabitNotFound : RoomErrors()
        data object InvalidColorCode: RoomErrors()
        data object Unknown : RoomErrors()
    }

    //other category errors like api error


}
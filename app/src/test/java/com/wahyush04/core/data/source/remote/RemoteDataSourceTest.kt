package com.wahyush04.core.data.source.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.wahyush04.androidphincon.ui.adapter.ProductListAdapter
import com.wahyush04.core.api.ApiService
import com.wahyush04.core.data.*
import com.wahyush04.core.data.source.remote.response.login.LoginResponse
import com.wahyush04.core.data.source.remote.response.product.DataListProductPaging
import com.wahyush04.core.data.source.remote.response.updatestock.DataStockItem
import com.wahyush04.core.data.source.remote.response.updatestock.UpdateStockRequestBody
import com.wahyush04.utils.DataDummy
import com.wahyush04.utils.MainDispatcherRule
import com.wahyush04.utils.getOrAwaitValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule: MainDispatcherRule = MainDispatcherRule()


    @Mock
    private lateinit var apiService: ApiService

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = RemoteDataSource(apiService)
    }

    @Test
    fun `when Login Success `() = runTest {
        // Arrange
        `when`(
            apiService.userLogin(
                email = ArgumentMatchers.anyString(),
                password = ArgumentMatchers.anyString(),
                token_fcm = ArgumentMatchers.anyString()
            )
        )
            .thenReturn(DataDummy.generateLoginResponse())

        // Act
        val resultFlow = remoteDataSource.login(
            email = "",
            password = "",
            tokenFcm = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateLoginResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when Login is Error 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        `when`(
            apiService.userLogin(
                email = ArgumentMatchers.anyString(),
                password = ArgumentMatchers.anyString(),
                token_fcm = ArgumentMatchers.anyString()
            )
        )
            .thenThrow(HttpException(response))


        // Act
        val resultFlow = remoteDataSource.login(
            email = "",
            password = "",
            tokenFcm = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when Login is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        `when`(
            apiService.userLogin(
                email = ArgumentMatchers.anyString(),
                password = ArgumentMatchers.anyString(),
                token_fcm = ArgumentMatchers.anyString()
            )
        ).thenThrow(HttpException(response))


        // Act
        val resultFlow = remoteDataSource.login(
            email = "",
            password = "",
            tokenFcm = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when Login is Other Error`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(500, "".toResponseBody(null))
        `when`(
            apiService.userLogin(
                email = ArgumentMatchers.anyString(),
                password = ArgumentMatchers.anyString(),
                token_fcm = ArgumentMatchers.anyString()
            )
        )
            .thenThrow(HttpException(response))


        // Act
        val resultFlow = remoteDataSource.login(
            email = "",
            password = "",
            tokenFcm = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when Register Response Success`() = runTest {
        // Arrange
        val dataDummy = DataDummy.generateRegisterResponse()
        val imageDummy = MultipartBody.Part.create("text".toRequestBody())
        val emailDummy = "emailDummy@gmail.com".toRequestBody()
        val passwordDummy = "passwordDummy".toRequestBody()
        val nameDummy = "nameDummy".toRequestBody()
        val phoneDummy = "phoneDummy".toRequestBody()
        val genderDummy = 0

        `when`(
            apiService.userRegister(
                nameDummy,
                emailDummy,
                passwordDummy,
                phoneDummy,
                genderDummy,
                imageDummy
            )
        ).thenReturn(dataDummy)

        // Act
        val resultFlow = remoteDataSource.register(
            nameDummy,
            emailDummy,
            passwordDummy,
            phoneDummy,
            genderDummy,
            imageDummy
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateRegisterResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when Register is Error 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        val imageDummy = MultipartBody.Part.create("text".toRequestBody())
        val emailDummy = "emailDummy@gmail.com".toRequestBody()
        val passwordDummy = "passwordDummy".toRequestBody()
        val nameDummy = "nameDummy".toRequestBody()
        val phoneDummy = "phoneDummy".toRequestBody()
        val genderDummy = 0

        `when`(
            apiService.userRegister(
                nameDummy,
                emailDummy,
                passwordDummy,
                phoneDummy,
                genderDummy,
                imageDummy
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.register(
            nameDummy,
            emailDummy,
            passwordDummy,
            phoneDummy,
            genderDummy,
            imageDummy
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when Register is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        val imageDummy = MultipartBody.Part.create("text".toRequestBody())
        val emailDummy = "emailDummy@gmail.com".toRequestBody()
        val passwordDummy = "passwordDummy".toRequestBody()
        val nameDummy = "nameDummy".toRequestBody()
        val phoneDummy = "phoneDummy".toRequestBody()
        val genderDummy = 0
        `when`(
            apiService.userRegister(
                nameDummy,
                emailDummy,
                passwordDummy,
                phoneDummy,
                genderDummy,
                imageDummy
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.register(
            nameDummy,
            emailDummy,
            passwordDummy,
            phoneDummy,
            genderDummy,
            imageDummy
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when Register is Error 500`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(500, "".toResponseBody(null))
        val imageDummy = MultipartBody.Part.create("text".toRequestBody())
        val emailDummy = "emailDummy@gmail.com".toRequestBody()
        val passwordDummy = "passwordDummy".toRequestBody()
        val nameDummy = "nameDummy".toRequestBody()
        val phoneDummy = "phoneDummy".toRequestBody()
        val genderDummy = 0

        `when`(
            apiService.userRegister(
                nameDummy,
                emailDummy,
                passwordDummy,
                phoneDummy,
                genderDummy,
                imageDummy
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.register(
            nameDummy,
            emailDummy,
            passwordDummy,
            phoneDummy,
            genderDummy,
            imageDummy
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get Favorite Product Success`() = runTest {
        // Arrange
        `when`(
            apiService.getFavorite(
                search = "",
                id_user = 0
            )
        ).thenReturn(DataDummy.generateProductListResponse())

        // Act
        val resultFlow = remoteDataSource.getFavoriteProduct(
            userId = 0,
            search = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateProductListResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when get Favorite is Error 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        `when`(
            apiService.getFavorite(
                search = "",
                id_user = 0
            )
        )
            .thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getFavoriteProduct(
            userId = 0,
            search = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get Favorite is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        `when`(
            apiService.getFavorite(
                search = "",
                id_user = 0
            )
        )
            .thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getFavoriteProduct(
            userId = 0,
            search = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get Favorite is Error 500`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(500, "".toResponseBody(null))
        `when`(
            apiService.getFavorite(
                search = "",
                id_user = 0
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getFavoriteProduct(
            userId = 0,
            search = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when change password Success`() = runTest {
        // Arrange
        `when`(
            apiService.userChangePassword(
                id = "1",
                password = "password",
                new_Password = "new_password",
                confirm_password = "confirm_passwrod"

            )
        ).thenReturn(DataDummy.generateChangePasswordResponse())

        // Act
        val resultFlow = remoteDataSource.changePassword(
            id = 1,
            password = "password",
            newPassword = "new_password",
            confirmPassword = "confirm_passwrod"
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateChangePasswordResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when change password is Error 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        `when`(
            apiService.userChangePassword(
                id = "1",
                password = "password",
                new_Password = "new_password",
                confirm_password = "confirm_passwrod"

            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.changePassword(
            id = 1,
            password = "password",
            newPassword = "new_password",
            confirmPassword = "confirm_passwrod"
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when change password is Error 401`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(401, "".toResponseBody(null))
        `when`(
            apiService.userChangePassword(
                id = "1",
                password = "password",
                new_Password = "new_password",
                confirm_password = "confirm_passwrod"

            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.changePassword(
            id = 1,
            password = "password",
            newPassword = "new_password",
            confirmPassword = "confirm_passwrod"
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when change password is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        `when`(
            apiService.userChangePassword(
                id = "1",
                password = "password",
                new_Password = "new_password",
                confirm_password = "confirm_passwrod"

            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.changePassword(
            id = 1,
            password = "password",
            newPassword = "new_password",
            confirmPassword = "confirm_passwrod"
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when change password is Error 404`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(404, "".toResponseBody(null))
        `when`(
            apiService.userChangePassword(
                id = "1",
                password = "password",
                new_Password = "new_password",
                confirm_password = "confirm_passwrod"

            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.changePassword(
            id = 1,
            password = "password",
            newPassword = "new_password",
            confirmPassword = "confirm_passwrod"
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when change image Success`() = runTest {
        // Arrange
        val id = "id".toRequestBody()
        val fileContent = "Hello, world!".toByteArray()
        val fileRequestBody = fileContent.toRequestBody("multipart/form-data".toMediaType())
        val filePart = MultipartBody.Part.createFormData("file", "test.txt", fileRequestBody)
        `when`(
            apiService.changeImage(
                id,
                filePart
            )
        ).thenReturn(DataDummy.generateChangeImageResponse())

        // Act
        val resultFlow = remoteDataSource.changeImage(
            id,
            filePart
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateChangeImageResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when change image is Error 401`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(401, "".toResponseBody(null))
        val id = "id".toRequestBody()
        val fileContent = "Hello, world!".toByteArray()
        val fileRequestBody = fileContent.toRequestBody("multipart/form-data".toMediaType())
        val filePart = MultipartBody.Part.createFormData("file", "test.txt", fileRequestBody)
        `when`(
            apiService.changeImage(
                id,
                filePart
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.changeImage(
            id,
            filePart
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when change image is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        val id = "id".toRequestBody()
        val fileContent = "Hello, world!".toByteArray()
        val fileRequestBody = fileContent.toRequestBody("multipart/form-data".toMediaType())
        val filePart = MultipartBody.Part.createFormData("file", "test.txt", fileRequestBody)
        `when`(
            apiService.changeImage(
                id,
                filePart
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.changeImage(
            id,
            filePart
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when change image is Error 500`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(500, "".toResponseBody(null))
        val id = "id".toRequestBody()
        val fileContent = "Hello, world!".toByteArray()
        val fileRequestBody = fileContent.toRequestBody("multipart/form-data".toMediaType())
        val filePart = MultipartBody.Part.createFormData("file", "test.txt", fileRequestBody)
        `when`(
            apiService.changeImage(
                id,
                filePart
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.changeImage(
            id,
            filePart
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get detail product data Success`() = runTest {
        // Arrange
        `when`(
            apiService.getDetailProduct(
                1,
                1
            )
        )
            .thenReturn(DataDummy.generateDetailProductResponse())

        // Act
        val resultFlow = remoteDataSource.detailProduct(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateDetailProductResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when get detail product data is Error 401`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(401, "".toResponseBody(null))
        `when`(
            apiService.getDetailProduct(
                1,
                1
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.detailProduct(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get detail product data is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        `when`(
            apiService.getDetailProduct(
                1,
                1
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.detailProduct(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get detail product data is Error 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        `when`(
            apiService.getDetailProduct(
                1,
                1
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.detailProduct(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when add favorite success`() = runTest {
        // Arrange
        `when`(
            apiService.addFavorite(
                1,
                1
            )
        ).thenReturn(DataDummy.generateAddFavoriteResponse())

        // Act
        val resultFlow = remoteDataSource.addFavorite(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateAddFavoriteResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when add favorite is 401`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(401, "".toResponseBody(null))
        `when`(
            apiService.addFavorite(
                1,
                1
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.addFavorite(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when add favorite is 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        `when`(
            apiService.addFavorite(
                1,
                1
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.addFavorite(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when add favorite is 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        `when`(
            apiService.addFavorite(
                1,
                1
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.addFavorite(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }


    @Test
    fun `when remove favorite success`() = runTest {
        // Arrange
        `when`(
            apiService.removeFavorite(
                1,
                1
            )
        ).thenReturn(DataDummy.generateAddFavoriteResponse())

        // Act
        val resultFlow = remoteDataSource.removeFavorite(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateAddFavoriteResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when remove favorite is Error 401`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(401, "".toResponseBody(null))
        `when`(
            apiService.removeFavorite(
                1,
                1
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.removeFavorite(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when remove favorite is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        `when`(
            apiService.removeFavorite(
                1,
                1
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.removeFavorite(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when remove favorite is Error 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        `when`(
            apiService.removeFavorite(
                1,
                1
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.removeFavorite(
            1,
            1
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get Other Product Response Success`() = runTest {
        // Arrange

        `when`(
            apiService.getOtherProducts(1)
        ).thenReturn(DataDummy.generateProductListResponse())

        // Act
        val resultFlow = remoteDataSource.getOtherProduk(1)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
//            assertTrue(awaitItem() is Result.Success)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateProductListResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when get Other Product Response is Error 401`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(401, "".toResponseBody(null))
        `when`(
            apiService.getOtherProducts(1)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getOtherProduk(1)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get Other Product Response is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        `when`(
            apiService.getOtherProducts(1)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getOtherProduk(1)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get Other Product Response is Error 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        `when`(
            apiService.getOtherProducts(1)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getOtherProduk(1)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get History Product Response Success`() = runTest {
        // Arrange
        `when`(
            apiService.getProductSearchHistory(1)
        ).thenReturn(DataDummy.generateProductListResponse())

        // Act
        val resultFlow = remoteDataSource.getHistoryProduk(1)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateProductListResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when get History Product Response is Error 401`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(401, "".toResponseBody(null))
        `when`(
            apiService.getProductSearchHistory(1)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getHistoryProduk(1)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get History Product Response is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        `when`(
            apiService.getProductSearchHistory(1)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getHistoryProduk(1)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get History Product Response is Error 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        `when`(
            apiService.getProductSearchHistory(1)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getHistoryProduk(1)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when get History Product Response is Error 500`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(500, "".toResponseBody(null))
        `when`(
            apiService.getProductSearchHistory(1)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.getHistoryProduk(1)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when buy Product Success`() = runTest {
        val requestBody = UpdateStockRequestBody(
            "1",
            listOf(
                DataStockItem("1", 10),
                DataStockItem("2", 10)
            )
        )
        // Arrange
        `when`(
            apiService.buyProduct(requestBody)
        ).thenReturn(DataDummy.generateUpdateStockResponse())

        // Act
        val resultFlow = remoteDataSource.buyProduct(requestBody)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateUpdateStockResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when buy Product is Error 400`() = runTest {
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        val requestBody = UpdateStockRequestBody(
            "1",
            listOf(
                DataStockItem("1", 10),
                DataStockItem("2", 10)
            )
        )
        // Arrange
        `when`(
            apiService.buyProduct(requestBody)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.buyProduct(requestBody)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when buy Product is Error 401`() = runTest {
        val response = Response.error<LoginResponse>(401, "".toResponseBody(null))
        val requestBody = UpdateStockRequestBody(
            "1",
            listOf(
                DataStockItem("1", 10),
                DataStockItem("2", 10)
            )
        )
        // Arrange
        `when`(
            apiService.buyProduct(requestBody)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.buyProduct(requestBody)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when buy Product is Error 429`() = runTest {
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        val requestBody = UpdateStockRequestBody(
            "1",
            listOf(
                DataStockItem("1", 10),
                DataStockItem("2", 10)
            )
        )
        // Arrange
        `when`(
            apiService.buyProduct(requestBody)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.buyProduct(requestBody)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when buy Product is Error 500`() = runTest {
        val response = Response.error<LoginResponse>(500, "".toResponseBody(null))
        val requestBody = UpdateStockRequestBody(
            "1",
            listOf(
                DataStockItem("1", 10),
                DataStockItem("2", 10)
            )
        )
        // Arrange
        `when`(
            apiService.buyProduct(requestBody)
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.buyProduct(requestBody)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when rating product Success`() = runTest {
        // Arrange
        `when`(
            apiService.updateRating(1, "4.5")
        ).thenReturn(DataDummy.generateUpdateRatingResponse())

        // Act
        val resultFlow = remoteDataSource.updateRating(1, "4.5")

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            val data = awaitItem() as Result.Success
            assertEquals(DataDummy.generateUpdateRatingResponse(), data.data)
            awaitComplete()
        }
    }

    @Test
    fun `when rating product is Error 401`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(401, "".toResponseBody(null))
        `when`(
            apiService.updateRating(1, "4.5")
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.updateRating(1, "4.5")

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when rating product is Error 429`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(429, "".toResponseBody(null))
        `when`(
            apiService.updateRating(1, "4.5")
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.updateRating(1, "4.5")

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when rating product is Error 400`() = runTest {
        // Arrange
        val response = Response.error<LoginResponse>(400, "".toResponseBody(null))
        `when`(
            apiService.updateRating(1, "4.5")
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = remoteDataSource.updateRating(1, "4.5")

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    @Test
    fun `when getProductList Paging isSuccess`() = runTest {
        val data = ProductPagingSource.snapshot(DataDummy.generateDataListProductPaging())
        val expectedProduct = MutableLiveData<PagingData<DataListProductPaging>>()
        expectedProduct.value = data

        CoroutineScope(Dispatchers.IO).launch {
            val actualData: PagingData<DataListProductPaging> =
                remoteDataSource.getProduct("").getOrAwaitValue()
            val differ = AsyncPagingDataDiffer(
                diffCallback = ProductListAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main
            )
            differ.submitData(actualData)

            assertNotNull(differ.snapshot())
            assertEquals(DataDummy.generateDataListProductPaging().size, differ.snapshot().size)
            assertEquals(
                DataDummy.generateDataListProductPaging()[0].nameProduct,
                differ.snapshot()[0]?.nameProduct
            )
        }
    }

    class ProductPagingSource : PagingSource<Int, LiveData<List<DataListProductPaging>>>() {
        companion object {
            fun snapshot(items: List<DataListProductPaging>): PagingData<DataListProductPaging> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<DataListProductPaging>>>): Int? {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<DataListProductPaging>>> {
            return PagingSource.LoadResult.Page(emptyList(), 0, 1)
        }

    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}
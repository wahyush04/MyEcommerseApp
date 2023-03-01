package com.wahyush04.androidphincon.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wahyush04.core.data.source.repository.Repository
import com.wahyush04.utils.DataDummy
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var viewModel: RegisterViewModel
    private val dummyNews = DataDummy.generateRegisterResponse()

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(repository)
    }

//    @Test
//    fun `when Register Success`() {
//        val expectedData = MutableLiveData<Result<RegisterResponse>>()
//        expectedData.value = Result.Success(dummyNews)
//        val requestBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("myData", "multipart")
//            .build()
//        Mockito.`when`(repository.register(
//            requestBody.part(0),
//            "email".toRequestBody(),
//            "password".toRequestBody(),
//            "name".toRequestBody(),
//            "phone".toRequestBody(),
//            1
//        )).thenReturn(expectedData)
//
//        val actualData = viewModel.register(
//            "name".toRequestBody(),
//            "email".toRequestBody(),
//            "password".toRequestBody(),
//            "phone".toRequestBody(),
//            1,
//            requestBody.part(0),
//        ).getOrAwaitValue()
//
//        Mockito.verify(repository).register(
//            requestBody.part(0),
//            "email".toRequestBody(),
//            "password".toRequestBody(),
//            "name".toRequestBody(),
//            "phone".toRequestBody(),
//            1
//        )
//        Assert.assertNotNull(actualData)
//        Assert.assertTrue(actualData is Result.Success)
//        Assert.assertEquals(dummyNews.registerSuccess, (actualData as Result.Success).data.registerSuccess)
//    }

}
package com.wahyush04.androidphincon.ui.login

//@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(MockitoJUnitRunner::class)
//class LoginViewModelTest {
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    private lateinit var repository: Repository
//    private lateinit var viewModel: LoginViewModel
//    private val dummyLoginResponse = DataDummy.generateLoginResponse()
//
//    @Before
//    fun setUp() {
//        viewModel = LoginViewModel(repository)
//    }
//
//
//    @Test
//    fun `when Login Success`() =  runBlockingTest {
//        val expectedData = MutableLiveData<Result<LoginResponse>>()
//        expectedData.value = Result.Success(dummyLoginResponse)
//        `when`(repository.login("email@gmail.com", "password", "token_fcm")).thenReturn(expectedData)
//
//        val actualData = viewModel.login("email@gmail.com", "password", "token_fcm").getOrAwaitValue()
//        Mockito.verify(repository).login("email@gmail.com", "password", "token_fcm")
//        assertNotNull(actualData)
//        assertTrue(actualData is Result.Success)
//        assertEquals(dummyLoginResponse.success.data_user, (actualData as Result.Success).data.success.data_user)
//    }
//
//    @Test
//    fun `when Error Should Return Error`() {
//        val expectedData = MutableLiveData<Result<LoginResponse>>()
//        expectedData.value = Result.Error(true,401, "error".toResponseBody())
//        `when`(repository.login("email@gmail.com", "password", "token_fcm")).thenReturn(expectedData)
//
//        val actualData = viewModel.login("email@gmail.com", "password", "token_fcm").getOrAwaitValue()
//        Mockito.verify(repository).login("email@gmail.com", "password", "token_fcm")
//        Assert.assertNotNull(actualData)
//        Assert.assertTrue(actualData is Result.Error)
//    }
//}
package sec01.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class MemberController
 */
@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MemberDAO memberDAO;

	public void init() throws ServletException {
		memberDAO = new MemberDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action = request.getPathInfo();
		System.out.println("action:" + action);
		
		// listMembers.do 로 입력 했을 때
		if (action == null || action.equals("/listMembers.do")) {
			// DAO의 출력 메서드 실행
			List<MemberVO> membersList = memberDAO.listMembers();
			// 메서드로 부터 반환된 memberList 바인딩 ==> memberList 변수에 반환 된 memberList
			request.setAttribute("membersList", membersList);
			// listMembers.jsp => MVC 중 VIEW에 해당, 회원조회를 담당하는 VIEW로 포워딩 준비.
			nextPage = "/test03/listMembers.jsp";
		
		// addMember.do 로 입력했을 때 (회원 추가)
		} else if (action.equals("/addMember.do")) {
			// 각 속성 값 받아오기.
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			// 각 속성 VO 저장 후, DAO의 회원 추가 메서드실행(매개변수 MemberVO(각 속성을 저장한))
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			memberDAO.addMember(memberVO);
			// msg 변수에 문자열 "addMember" 바인딩
			request.setAttribute("msg", "addMember");
			// listMembers.jsp => MVC 중 VIEW에 해당, 회원조회를 담당하는 VIEW로 포워딩 준비.
			nextPage = "/member/listMembers.do";
		
		// memberForm.do 로 입력했을 때 (회원 등록)
		} else if (action.equals("/memberForm.do")) {
			//회원 등록 VIEW로 포워딩 준비
			nextPage = "/test03/memberForm.jsp";
			
		// modMemberForm.do 로 입력했을 때 (회원 수정)
		}else if(action.equals("/modMemberForm.do")){
			// id 값 받아오기
		     String id=request.getParameter("id");
		     // 받아온 id 값으로 DAO의 회원찾기 메서드 (매개변수 = id)
		     MemberVO memInfo = memberDAO.findMember(id);
		     // findMember()메서드로 부터 반환된 memInfo를 memInfo변수에  바인딩 
		     // memInfo는 VO타입의 매개변수id 에 해당되는 칼럼의 각 속성의 값들이 저장되어있는 객체 
		     request.setAttribute("memInfo", memInfo);
		     // modMemberForm.jsp => MVC 중 VIEW에 해당, 회원 수정을 담당하는 VIEW로 포워딩 준비.
		     nextPage="/test03/modMemberForm.jsp";
		
		// modMember.do로 입력 했을 때
		}else if(action.equals("/modMember.do")){
			 // 각 속성들 값 받아온 후 , VO에 저장
		     String id=request.getParameter("id");
		     String pwd=request.getParameter("pwd");
		     String name= request.getParameter("name");
	         String email= request.getParameter("email");
		     MemberVO memberVO = new MemberVO(id, pwd, name, email);
		     // DAO의 회원 수정 메서드 실행.
		     memberDAO.modMember(memberVO);
		     // 변수 "msg"에 문자열 "modified"을 바인딩
		     request.setAttribute("msg", "modified");
		     // listMembers.jsp => MVC 중 VIEW에 해당, 회원조회를 담당하는 VIEW로 포워딩 준비.
		     nextPage="/member/listMembers.do";
		     
		// delMember.do 로 입력했을 때
		}else if(action.equals("/delMember.do")){
			// id 값 받아온 후, DAO의 삭제 메서드 실행
		     String id=request.getParameter("id");
		     memberDAO.delMember(id);
		     // 변수 "msg"에 값 "deleted" 바인딩
		     request.setAttribute("msg", "deleted");
		     // listMembers.jsp => MVC 중 VIEW에 해당, 회원조회를 담당하는 VIEW로 포워딩 준비.
		     nextPage="/member/listMembers.do";
		}else {
		//그 외 멤버조회 메서드 실행
			List<MemberVO> membersList = memberDAO.listMembers();
			// 메서드로 부터 반환된 memberlist 바인딩 후 회원 조회 VIEW 포워딩 준비.
			request.setAttribute("membersList", membersList);
			nextPage = "/test03/listMembers.jsp";
		}
		// 포워딩 준비된 nextpage을 매개변수로 포워딩.
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}

}

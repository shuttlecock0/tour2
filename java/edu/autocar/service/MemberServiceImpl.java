package edu.autocar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.autocar.dao.MemberDao;
import edu.autocar.domain.Member;
import edu.autocar.domain.PageInfo;
import edu.autocar.util.SHA256Util;

@Service
public class MemberServiceImpl implements MemberService {
	final static private int PER_PAGE_COUNT = 10;

	@Autowired
	MemberDao dao;
	@Autowired
	AvataService avataService;
	
	@Transactional
	@Override
	public PageInfo<Member> getPage(int page) throws Exception {
		int start = (page - 1) * PER_PAGE_COUNT;
		int end = start + PER_PAGE_COUNT;
		int totalCount = dao.count();
		List<Member> list = dao.getPage(start, end);
		return new PageInfo<>(totalCount, (int) Math.ceil(totalCount / (double) PER_PAGE_COUNT), page, PER_PAGE_COUNT,
				list);
	}

	@Override
	public Member getMember(String userId) throws Exception {
		return dao.findById(userId);
	}

	@Transactional
	@Override
	public void create(Member member, MultipartFile file) throws Exception {
		// salt 생성 및 비밀번호 암호화
		String salt = SHA256Util.generateSalt();
		String encPassword = SHA256Util.getEncrypt(member.getPassword(), salt);

		member.setSalt(salt);
		member.setPassword(encPassword);

		dao.insert(member);
		avataService.create(member.getUserId(), file);
	}

	@Transactional
	@Override
	public boolean update(Member member, MultipartFile file) throws Exception {
		if (checkPassword(member.getUserId(), member.getPassword()) == null)
			return false;
		
		if(dao.update(member) == 0)
			return false;
		//아바타 수정
		return avataService.update(member.getUserId(), file);
	}

	@Transactional
	@Override
	public boolean updateByAdmin(Member member, MultipartFile file) throws Exception {
		if (!checkAdminPassword(member.getPassword()))
			return false;
		
		if(dao.updateByAdmin(member) != 1)
			return false;
		
		//아바타 수정
		avataService.update(member.getUserId(), file);
		return true;
	}

	private boolean checkAdminPassword(String password) throws Exception {
		Member admin = dao.findById("admin");
		String adminPassword = admin.getPassword();
		password = SHA256Util.getEncrypt(password, // 입력받은 비밀번호
				admin.getSalt()); // admin의 salt
		return adminPassword.equals(password);
	}

	@Transactional
	@Override
	public boolean delete(String userId, String password) throws Exception {
		if (!checkAdminPassword(password))
			return false;
		return dao.delete(userId) == 1;
	}

	@Override
	public Member checkPassword(String userId, String password) throws Exception {
		Member user = dao.findById(userId);
		if (user != null) { // 사용자 ID가 존재하는 경우
			password = SHA256Util.getEncrypt(password, user.getSalt());
			if (password.equals(user.getPassword()))
				return user;
		}
		// ID가 없거나 비밀번호가 다른 경우
		return null;
	}

}

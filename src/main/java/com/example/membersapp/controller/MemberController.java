package com.example.membersapp.controller;

import com.example.membersapp.model.Member;
import com.example.membersapp.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

  @Autowired private MemberRepository memberRepository;

  @GetMapping
  public List<Member> getAllMembers() {
    return memberRepository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
    Optional<Member> member = memberRepository.findById(id);
    return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Member> createMember(@RequestBody Member member) {
    Member savedMember = memberRepository.save(member);
    return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Member> updateMember(
      @PathVariable Long id, @RequestBody Member memberDetails) {
    Optional<Member> member = memberRepository.findById(id);
    if (member.isPresent()) {
      Member existingMember = member.get();
      existingMember.setFirstName(memberDetails.getFirstName());
      existingMember.setLastName(memberDetails.getLastName());
      existingMember.setBirthDate(memberDetails.getBirthDate());
      Member updatedMember = memberRepository.save(existingMember);
      return ResponseEntity.ok(updatedMember);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
    if (memberRepository.existsById(id)) {
      memberRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}

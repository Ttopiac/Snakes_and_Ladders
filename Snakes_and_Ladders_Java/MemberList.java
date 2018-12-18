import java.util.*;

class MemberList extends LinkedList<Member> {
	int AddMember(String acc, String pass)
	{
		Member newMember = new Member( acc, pass);
		Member member;
		if( this.size() == 0){
			this.add( newMember);
		}
		else{
			for(int i=0; i<this.size(); i++){
				member = this.get(i);
				if( member.account.compareTo( newMember.account) == 0){
					return -2;  //same account and return -1
				}
			}
			this.add( newMember);
		}
		System.out.println("Account: " + acc + "\nPass: " + pass + "\n");
		return this.size()-1; //Register Successfully and return Member Index
	}
	int Authentication( String acc, String pass){
		for(int memberIndex=0; memberIndex<this.size(); memberIndex++){
			Member member = this.get( memberIndex);
			if( member.account.compareTo( acc) == 0){
				if( member.password.compareTo( pass) == 0){
					if( member.onLineState == false){
						member.onLineState = true;
						return memberIndex;	//Authentication Successfully and return Member Index
					}
					return -3;	//wrong state(member have been on line) and return Member Index
				}
				else{
					return -1; //wrong password and return -1
				}
			}
		}
		return -2; //wrong account and return -2
	}
}

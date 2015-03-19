package my.club;

import java.util.ArrayList;
import java.util.Locale;

import database.dbhelper;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class TranslatorActivity extends Activity implements OnItemSelectedListener  {
    /** Called when the activity is first created. */
	EditText t1,t2;
	Button b1;
	Cursor cr;
	String sms,trastring,hindi1;
    String[] sent,word,category,temp;
    public String language;
  //  public TextToSpeech tts;
   
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.planets_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
        
		final dbhelper myDbHelper=new dbhelper(this);
    
	//	    tts = new TextToSpeech(this, this);
		    //btnSpeak = (Button) findViewById(R.id.imageButton2);
           // txtText = (EditText) findViewById(R.id.speak);
            b1=(Button)findViewById(R.id.b1);
            t1=(EditText)findViewById(R.id.english);
            t2=(EditText)findViewById(R.id.hindi);
            
         //   Typeface type = Typeface.createFromAsset(getAssets(),"shusha.ttf"); 
           // t2.setTypeface(type);
            
            
       //     btnSpeak.setOnClickListener(new View.OnClickListener() {

    	//		@Override
    	//		public void onClick(View arg0) {
    		//		speakOut();
    	//		}

    //		});
            
            try {
            	 myDbHelper.createDataBase();
           	     
           	     myDbHelper.openDataBase();
          	     String st=("Welcome in English to Hindi/Marathi/Bangla Translator");
            	 Toast.makeText(this, st, Toast.LENGTH_LONG).show();
            	 
            	  
             	 b1.setOnClickListener(new View.OnClickListener() {
                     public void onClick(View v) 
                     {  

                         hindi1="";
                         //Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
                         ArrayList<Tree> le=new ArrayList<Tree>();
                         ArrayList<String> catar=new ArrayList<String>();
                         ArrayList<String> hindi=new ArrayList<String>();
                         
                         sms=t1.getText().toString();
                         sms=sms+" ";
                         sent=getSentence(sms);
                         
                         //Toast.makeText(getApplicationContext(), sms, Toast.LENGTH_SHORT).show();
                                
                        for(int i=0;i<sent.length;i++)
                         {
                        	//Toast.makeText(getApplicationContext(), sent[i], Toast.LENGTH_SHORT).show();
                             le.clear();
                             catar.clear();
                             hindi.clear();
                             word=getWord(sent[i]);
                             //Toast.makeText(getApplicationContext(), word[2], Toast.LENGTH_SHORT).show();
                         
                         for(int j=0;j<word.length;j++)
                         {
                        	 //Toast.makeText(getApplicationContext(), word[j], Toast.LENGTH_SHORT).show();
                        	 
                         	myDbHelper.englishword=word[j].toLowerCase();
                         	 myDbHelper.lang_choose = language;
                         	cr=myDbHelper.getcur();
                             if(cr.moveToNext())
                             {
                                 catar.add(cr.getString(0));
                                 hindi.add((cr.getString(1)));
                                                                  
                             }                     
                         
                         }
                         //Toast.makeText(getApplicationContext(), catar.get(2), Toast.LENGTH_SHORT).show();
                         
                         for(int t=0;t<catar.size();t++)
                         {
                        	 //Toast.makeText(getApplicationContext(), catar.get(t), Toast.LENGTH_SHORT).show();
                             
                             le.add(new Tree(catar.get(t)));
                         }
                        
                     Tree[] tr=(Tree[])le.toArray(new Tree[le.size()]);
                     //Toast.makeText(getApplicationContext(), tr[0].s, Toast.LENGTH_SHORT).show();
                         
                      
                         Tree se=getS(tr);
                         
                        for(int x=0;x<tr.length;x++)
                         {
                             tr[x].t1=new Tree(hindi.get(x));
                             
                         }
                        //display(se); 
                         
                         
                       if(word.length>2)
                          treeTranslat(se);
                        
                         display(se);       
                         
                         hindi1+=". ";   
                         }       
                         t2.setText(hindi1);             	 
                    	 
                    	 
                     }
                     });   
             	 
             	 
            }
                   
            catch (android.database.SQLException ioe) 
            {
            	String st=("Unable to open database111: "+ioe.getMessage());
            	Toast tt=Toast.makeText(this, st, Toast.LENGTH_LONG);
            	tt.show();
            }
            catch (Exception ioe) 
            {
            	String st=("Unable to create/open database: "+ioe.getMessage());
            	Toast tt=Toast.makeText(this, st, Toast.LENGTH_LONG);
            	tt.show();
            	
            }
         
            
           
        
    }
    
                                
                           //method to be called
 
    String[] getSentence(String s)
    {
        String[] t=s.split("\\. ");
        return t;
    }
    String[] getWord(String s)
    {
        String[] t=s.split(" ");
        return t;
    }
    String[] getCategory(String s)
    {
        String[] t=s.split("\t");
        return t;
    }
    Tree getS(Tree[] s)
    {
        ArrayList<Tree> np1=new ArrayList<Tree>();
        ArrayList<Tree> vp1=new ArrayList<Tree>();
       
        int i,j;
        for(i=0;i<s.length;i++)
        {
            if(s[i].s.equals("V") || s[i].s.equals("AV"))
                break;
                
        }
        
        for(j=0;j<s.length;j++)
        {
            if(j<i)
               np1.add(s[j]);
            else
              vp1.add(s[j]); 
        }
       Tree np=getNP((Tree[])np1.toArray(new Tree[np1.size()]));
        Tree vp=getVP((Tree[])vp1.toArray(new Tree[vp1.size()]));
        return (new Tree(np, "S", vp));
    }
    Tree getNP(Tree[] np)
    {
        int i=np.length;
        ArrayList<Tree> np2=new ArrayList<Tree>();
        Tree np1;
        switch(i)
        {
            case 0:
            {
                
               return null;
                
            }
            case 1:
            {
             
            return (new Tree(np[0], "NP"));
            }
           case 2:
           {
              
             if(np[0].s.equals("DT") && np[1].s.equals("N"))
                 return (new Tree(np[0], "NP", np[1]));
             else if(np[0].s.equals("PR") && np[1].s.equals("N"))
                 return (new Tree(np[0], "NP", np[1]));
             else if(np[0].s.equals("AD") && np[1].s.equals("N"))
                 return (new Tree(np[0], "NP", np[1]));
           }
           default:
           {
                   
           if(np[0].s.equals("P") || np[0].s.equals("PR") || np[0].s.equals("AD")|| np[0].s.equals("DT"))
            {
                                              
                       for(int j=0;j<i-1;j++)
                           np2.add(np[j+1]);
                       np1=getNP((Tree[])np2.toArray(new Tree[np2.size()]));
                       return (new Tree(np[0], "NP", np1));
                   }
               }
                
            
        }
        return null;
    }
    Tree getVP(Tree[] vp)
    {
        int i=vp.length;
       
        ArrayList<Tree> np1=new ArrayList<Tree>();
        Tree np2=null,v,av;
        switch(i)
        {
            case 1:
            {
              return (new Tree(vp[0], "VP"));   
            }
            
            default :
            {
                int x=0;
               for(x=0;x<vp.length;x++)
               {
                   if(vp[x].s.equals("V"))
                       break;
               }
               if(x==vp.length)
               {
                 x=0;  
               }
               if(x==0)
               {
                   for(int j=x+1;j<i;j++)
                    {
                    np1.add(vp[j]);
                    }
                  np2=getNP((Tree[])np1.toArray(new Tree[np1.size()]));
                  return new Tree(vp[0], "VP", np2);
               }                   
               else
               {
                   if(x==1){
                   v=new Tree(vp[0],"V",vp[1]);
                    }
                   else
                   {
                     
                     av=new Tree(vp[0], "AV", vp[1]);
                     v=new Tree(av,"V",vp[2]);
                   }
                   for(int j=x+1;j<i;j++)
                    {
                    np1.add(vp[j]);
                    }
                  np2=getNP((Tree[])np1.toArray(new Tree[np1.size()]));
                  
                   return new Tree(v, "VP", np2);
               }
            }
        
      }
    }
    
    void display(Tree t)
    {
       
        if(t.t1==null && t.t2==null)
        {
           
            hindi1+=t.s+" ";
           //Toast.makeText(getApplicationContext(), hindi1, Toast.LENGTH_SHORT);
            
        }
        if(t.t1!=null && t.t2!=null)
        {
            display(t.t1);
            display(t.t2);
        }
        else if(t.t1!=null && t.t2==null)
            display(t.t1);
        else if(t.t1==null && t.t2!=null)
            display(t.t2);
       
        
    }
    void treeTranslat(Tree node)
    {
        
        
        if(node.t1.s.equals("V") && node.t2.s.equals("NP"))
            {
                node.shwap(node);
            }
            else if(node.t1.s.equals("P") && node.t2.s.equals("NP"))
            {
                node.shwap(node);
            }
        else if(node.t1.s.equals("AV") && node.t2.s.equals("V"))
            {
                
                node.shwap(node);
            }
        else if(node.t1.s.equals("AV") && node.t2.s.equals("NP"))
            {
                node.shwap(node);
            }
        else if(node.t1.s.equals("AV") && node.t2.s.equals("AV"))
            {
                
                node.shwap(node);
            }
        
        if(node.t1!=null && node.t2!=null)
        {
            treeTranslat(node.t1);
            treeTranslat(node.t2);       
        }
        
    }




	@Override
	public void onItemSelected(AdapterView<?>  parent, View view, int pos,long id) {
		// TODO Auto-generated method stub
		Toast.makeText(parent.getContext(), 
				"Language Selected " + parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();
		
		language = parent.getItemAtPosition(pos).toString();
		
		}
	
	
		@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
		
		
	//	@Override
//		public void onDestroy() {
			// Don't forget to shutdown!
	//		if (tts != null) {
		//		tts.stop();
		//		tts.shutdown();
		//	}
		//	super.onDestroy();
	//	}


//	@Override
//	public void onInit(int status) {
	//	// TODO Auto-generated method stub
		//if (status == TextToSpeech.SUCCESS) {

			//int result = tts.setLanguage(Locale.ENGLISH);

			// tts.setPitch(5); // set pitch level

			// tts.setSpeechRate(2); // set speech speed rate

		//	if (result == TextToSpeech.LANG_MISSING_DATA
				//	|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
			//	Log.e("TTS", "Language is not supported");
		//	} else {
			//	btnSpeak.setEnabled(true);
				//speakOut();
		//	}

	//	} else {
		//	Log.e("TTS", "Initilization Failed");
	//	}
//	}
	
//	public void speakOut() {

	//	String text = txtText.getText().toString();

	//	tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//	}
}
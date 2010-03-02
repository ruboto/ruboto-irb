#prepare number for dialing
import "android.content.Intent"
import "android.net.Uri"

intent = Intent.new(Intent::ACTION_VIEW)
intent.setData(Uri.parse("tel:123"))
$activity.startActivity(intent)